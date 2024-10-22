create database s4

--phân quyền role
CREATE TABLE Roles (
    RoleID INT PRIMARY KEY IDENTITY(1,1),
    RoleName NVARCHAR(50) NOT NULL
);

-- admin
CREATE TABLE Admins (
    AdminID INT PRIMARY KEY IDENTITY(1,1),
    FullName NVARCHAR(100),
    Email NVARCHAR(100) NOT NULL UNIQUE,
    PasswordHash NVARCHAR(255),
    RoleID INT FOREIGN KEY REFERENCES Roles(RoleID)
);

-- nhân viên
CREATE TABLE Employees (
    EmployeeID INT PRIMARY KEY IDENTITY(1,1),
    FullName NVARCHAR(100),
    Email NVARCHAR(100) NOT NULL UNIQUE,
    PhoneNumber NVARCHAR(15),
    RoleID INT FOREIGN KEY REFERENCES Roles(RoleID)
);

-- Khách hàng 
CREATE TABLE Customers (
    CustomerID INT PRIMARY KEY IDENTITY(1,1),
    FullName NVARCHAR(100),
    Email NVARCHAR(100) NOT NULL UNIQUE,
    PasswordHash NVARCHAR(255),
    PhoneNumber NVARCHAR(15),
    Address NVARCHAR(255),
    CreatedAt DATETIME DEFAULT GETDATE(),
    RoleID INT FOREIGN KEY REFERENCES Roles(RoleID)
);

--  thông tin khách sạn
CREATE TABLE Hotel (
    HotelID INT PRIMARY KEY IDENTITY(1,1),
    HotelName NVARCHAR(100),
    Address NVARCHAR(200),
    City NVARCHAR(100)
);

-- chi nhánh khách sạn 
CREATE TABLE Branches (
    BranchID INT PRIMARY KEY IDENTITY(1,1),
    HotelID INT FOREIGN KEY REFERENCES Hotel(HotelID),
    BranchName NVARCHAR(100),
    Location NVARCHAR(255),
    CreatedAt DATETIME DEFAULT GETDATE()
);

-- Room 
CREATE TABLE Room (
    RoomID INT PRIMARY KEY IDENTITY(1,1),
    RoomNumber NVARCHAR(50),
    RoomType NVARCHAR(50),            -- Loại phòng (Standard, Deluxe, Suite)
    PriceDay DECIMAL(18,2),           -- Giá ban ngày
    PriceNight DECIMAL(18,2),         -- Giá ban đêm
    HotelID INT,
    FOREIGN KEY (HotelID) REFERENCES Hotel(HotelID)
);

-- Amenities 
CREATE TABLE Amenities (
    AmenityID INT PRIMARY KEY IDENTITY(1,1),
    AmenityName NVARCHAR(100),        -- Tên tiện nghi (wifi, ban công,...)
    Description NVARCHAR(200)
);

-- RoomAmenities - tiện nghi cho từng phòng
CREATE TABLE RoomAmenities (
    RoomID INT,
    AmenityID INT,
    PRIMARY KEY (RoomID, AmenityID),
    FOREIGN KEY (RoomID) REFERENCES Room(RoomID),
    FOREIGN KEY (AmenityID) REFERENCES Amenities(AmenityID)
);

-- Bảng Booking lưu thông tin đặt phòng
CREATE TABLE Booking (
    BookingID INT PRIMARY KEY IDENTITY(1,1),
    CustomerID INT,
    RoomID INT,
    CheckInDate DATETIME,
    CheckOutDate DATETIME,
    Adults INT,                     -- Số lượng người lớn
    Children INT,                   -- Số lượng trẻ em
    TotalPeople AS (Adults + Children), -- Tổng số người
    PaymentMethod NVARCHAR(50),     -- Phương thức thanh toán (Online, Offline)
    ConfirmBookingCode NVARCHAR(50),       -- Mã đặt phòng (tạo riêng cho mỗi booking)
    TotalPrice DECIMAL(18,2),       -- Tổng giá tiền
    Status NVARCHAR(50),            -- Trạng thái (Pending, Confirmed, Completed, Cancelled)
    FOREIGN KEY (CustomerID) REFERENCES Customers(CustomerID),
    FOREIGN KEY (RoomID) REFERENCES Room(RoomID)
);

-- Bảng Service - thông tin các dịch vụ 
CREATE TABLE Service (
    ServiceID INT PRIMARY KEY IDENTITY(1,1),
    ServiceName NVARCHAR(100),        -- Tên dịch vụ (Đưa đón, Spa, Đặt bàn nhà hàng, Tổ chức tiệc)
    Description NVARCHAR(500),
    BasePrice DECIMAL(18,2)           -- Giá cơ bản của dịch vụ
);

CREATE TABLE ServiceBookings (
    ServiceBookingID INT PRIMARY KEY IDENTITY(1,1),
    CustomerID INT FOREIGN KEY REFERENCES Customers(CustomerID),  -- Khách hàng đặt dịch vụ
    ServiceID INT FOREIGN KEY REFERENCES Service(ServiceID),  -- Dịch vụ được đặt
    BookingDate DATETIME DEFAULT GETDATE(),  -- Ngày đặt 
    ServiceDate DATETIME NOT NULL,  -- Ngày sử dụng
    Distance INT,  -- Khoảng cách (km)
    TimeOfDay TIME,  -- Thời gian sử dụng dịch vụ
    Quantity INT DEFAULT 1,  -- Số lượng (ví dụ: số người)
    TotalPrice DECIMAL(10, 2),  -- Tổng giá (sẽ tính toán dựa trên các quy tắc)
    Status NVARCHAR(20) DEFAULT 'Pending'  -- Trạng thái đặt dịch vụ
);

CREATE TABLE ServicePricingRules (
    PricingRuleID INT PRIMARY KEY IDENTITY(1,1),
    ServiceID INT FOREIGN KEY REFERENCES Service(ServiceID),  -- Dịch vụ áp dụng quy tắc
    DistanceRange NVARCHAR(50),  -- Khoảng cách (ví dụ: "0-10km", "10-20km")
    TimeRange NVARCHAR(50),  -- Thời gian áp dụng (ví dụ: "6:00-12:00", "12:00-18:00")
    BasePrice DECIMAL(10, 2),  -- Giá cơ bản
    PricePerKm DECIMAL(10, 2) DEFAULT 0,  -- Giá mỗi km (nếu có)
    TimeMultiplier DECIMAL(5, 2) DEFAULT 1.0  -- Hệ số giá theo thời gian (nếu giá thay đổi theo thời gian)
);

-- Voucher - mã giảm giá
CREATE TABLE Voucher (
    VoucherID INT PRIMARY KEY IDENTITY(1,1),
    VoucherCode NVARCHAR(50),          -- Mã voucher (code)
    Description NVARCHAR(200),
    DiscountPercent DECIMAL(5,2),      -- Phần trăm giảm giá
    StartDate DATETIME,                -- Ngày bắt đầu hiệu lực
    EndDate DATETIME,                  -- Ngày hết hạn
    IsActive BIT                      -- Trạng thái (Còn hiệu lực hay không)
);

--INSERT INTO ServicePricingRules (ServiceID, DistanceRange, TimeRange, BasePrice, PricePerKm, TimeMultiplier)
--VALUES
--(1, '0-10km', '6:00-12:00', 200000, 5000, 1.0),  -- Giá cơ bản cho khoảng cách 0-10km và thời gian từ 6:00 đến 12:00
--(1, '0-10km', '12:00-18:00', 200000, 5000, 1.2),  -- Giá tăng 20% cho thời gian từ 12:00 đến 18:00
--(1, '10-20km', '6:00-12:00', 300000, 7000, 1.0),  -- Giá cơ bản cho khoảng cách 10-20km và thời gian từ 6:00 đến 12:00
--(1, '10-20km', '12:00-18:00', 300000, 7000, 1.2);  -- Giá tăng 20% cho khoảng cách 10-20km và thời gian từ 12:00 đến 18:00

--Thêm dịch vụ mới
-- INSERT INTO Service (ServiceName, Description, BasePrice)
-- VALUES ('Laundry', 'Giặt là quần áo cho khách hàng', 100000),
       -- ('TourGuide', 'Hướng dẫn viên du lịch tại khách sạn', 500000);

--  tính giá cho dịch vụ Laundry (Giặt là)
INSERT INTO ServicePricingRules (ServiceID, DistanceRange, TimeRange, BasePrice, PricePerKm, TimeMultiplier)
VALUES (4, '0-5km', '8:00-20:00', 100000, 0, 1.0);

-- Dịch vụ giặt là có thể được thiết lập với giá cơ bản là 100.000 VND và không phụ thuộc vào quãng đường hoặc thời gian trong ngày.

-- Quy tắc tính giá cho dịch vụ TourGuide (Hướng dẫn viên du lịch)
-- INSERT INTO ServicePricingRules (ServiceID, DistanceRange, TimeRange, BasePrice, PricePerKm, TimeMultiplier)
-- VALUES (5, '0-10km', '6:00-18:00', 500000, 10000, 1.2);

-- Bảng Reviews 
CREATE TABLE Reviews (
    ReviewID INT PRIMARY KEY IDENTITY(1,1),
    CustomerID INT FOREIGN KEY REFERENCES Customers(CustomerID),
    ServiceID INT FOREIGN KEY REFERENCES Service(ServiceID),
    Rating INT CHECK (Rating >= 1 AND Rating <= 5),  
    ReviewText NVARCHAR(500),
    ReviewImageURL NVARCHAR(255), 
    CreatedAt DATETIME DEFAULT GETDATE()
);

-- Bảng Comments 
CREATE TABLE Comments (
    CommentID INT PRIMARY KEY IDENTITY(1,1),
    CustomerID INT FOREIGN KEY REFERENCES Customers(CustomerID),
    ReviewID INT FOREIGN KEY REFERENCES Reviews(ReviewID),
    CommentText NVARCHAR(500),
    CommentImageURL NVARCHAR(255), 
    CreatedAt DATETIME DEFAULT GETDATE()
);


SELECT * FROM Service WHERE ServiceID = 1;


--  PaymentMethod -  phương thức thanh toán
CREATE TABLE PaymentMethod (
    PaymentMethodID INT PRIMARY KEY IDENTITY(1,1),   -- Khóa chính tự tăng
    MethodName NVARCHAR(100) NOT NULL,               -- Tên phương thức (VNPay, ZaloPay, Offline)
    Description NVARCHAR(500)                        -- Mô tả chi tiết về phương thức thanh toán
);

--  Payment -  giao dịch thanh toán
CREATE TABLE Payment (
    PaymentID INT PRIMARY KEY IDENTITY(1,1),         -- Khóa chính tự tăng
    BookingID INT NOT NULL,                          -- Tham chiếu đến đơn đặt phòng
    PaymentMethodID INT NOT NULL,                    -- Tham chiếu đến phương thức thanh toán
    PaymentDate DATETIME DEFAULT GETDATE(),          -- Ngày thanh toán
    Amount DECIMAL(18, 2) NOT NULL,                  -- Số tiền thanh toán
    PaymentStatus NVARCHAR(50) DEFAULT 'Pending',    -- Trạng thái thanh toán (Pending, Completed, Failed)
    TransactionCode NVARCHAR(100),                   -- Mã giao dịch (nếu thanh toán online)
    Currency NVARCHAR(10) NOT NULL,                  -- Đơn vị tiền tệ (USD, EUR, VND,...)
    Description NVARCHAR(255),                       -- Mô tả giao dịch thanh toán
    FOREIGN KEY (BookingID) REFERENCES Booking(BookingID),        -- Khóa ngoại tham chiếu Booking
    FOREIGN KEY (PaymentMethodID) REFERENCES PaymentMethod(PaymentMethodID) -- Khóa ngoại tham chiếu phương thức thanh toán
);



