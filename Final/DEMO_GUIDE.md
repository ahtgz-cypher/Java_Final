# 🎬 Hướng Dẫn Demo - Hệ Thống Quản Lý Điểm Sinh Viên

## ✅ Ứng dụng đã được biên dịch và chạy thành công!

## 📋 Các bước demo:

### 1. **Đăng nhập với tài khoản có sẵn**

Từ file `database/data.sql`, các tài khoản mặc định:

#### 👨‍💼 **Admin:**
- Username: `admin1` hoặc `admin2`
- Password: `123456`

#### 👨‍🏫 **Giáo viên:**
- Username: `teacher1`, `teacher2`, `teacher3`, `teacher4`, `teacher5`
- Password: `123456`

#### 👨‍🎓 **Sinh viên:**
- Username: `sv01`, `sv02`, `sv03`, ... `sv15`
- Password: `123456`

---

### 2. **Demo chức năng Admin**

Sau khi đăng nhập với tài khoản Admin:

✅ **Trang chủ:**
- Xem thống kê tổng số sinh viên và giáo viên

✅ **Thêm Sinh Viên:**
- Nhập: Mã SV, Tên, Ngày sinh (YYYY-MM-DD), Username, Password
- Ví dụ: `SV100`, `Nguyen Van Test`, `2004-01-01`, `sv100`, `123456`

✅ **Thêm Giáo Viên:**
- Nhập: Tên giáo viên, Username, Password
- Thêm các môn học phụ trách (Tên môn, Số tín chỉ)
- Ví dụ: `Tran Van Test`, `teacher6`, `123456`, môn: `Lap trinh Python` (3 TC)

✅ **Xem Sinh Viên:**
- Xem danh sách tất cả sinh viên
- Có thể xóa sinh viên (sẽ xóa cả user tương ứng)

✅ **Xem Giáo Viên:**
- Xem danh sách tất cả giáo viên và môn học phụ trách
- Có thể xóa giáo viên (sẽ xóa cả môn học và user)

---

### 3. **Demo chức năng Giáo viên**

Sau khi đăng nhập với tài khoản Teacher:

✅ **Tìm kiếm sinh viên:**
- Tìm theo mã SV hoặc tên (ví dụ: `SV001` hoặc `Nguyen`)

✅ **Xem thông tin sinh viên:**
- Click vào một sinh viên trong bảng
- Xem thông tin chi tiết và bảng điểm hiện tại

✅ **Nhập điểm:**
1. Chọn sinh viên từ bảng
2. Chọn môn học (chỉ hiển thị môn của giáo viên này)
3. Nhập điểm (0-10)
4. Click "Nhập điểm"
- Nếu điểm đã tồn tại, sẽ tự động cập nhật

✅ **Xóa điểm:**
1. Chọn sinh viên và môn học
2. Click "Xóa điểm"
3. Xác nhận xóa

✅ **Xuất báo cáo:**
- **Xuất tất cả:** Xuất tất cả điểm trong hệ thống
- **Xuất theo môn:** Xuất điểm của môn học được chọn
- **Xuất sinh viên:** Xuất bảng điểm của sinh viên được chọn
- File CSV sẽ được lưu vào thư mục bạn chọn

---

### 4. **Demo chức năng Sinh viên**

Sau khi đăng nhập với tài khoản Student:

✅ **Xem thông tin cá nhân:**
- Họ tên, Mã SV, Ngày sinh, Username
- GPA tự động tính theo tín chỉ
- Tổng số tín chỉ đã học

✅ **Xem bảng điểm:**
- Danh sách tất cả môn học đã có điểm
- Hiển thị: STT, Tín chỉ, Môn học, Giáo viên, Điểm số, Điểm chữ (A-F)

✅ **Lọc và tìm kiếm:**
- **Lọc:** Tất cả, A-F, F-A, hoặc theo từng loại điểm (A, B, C, D, F)
- **Tìm kiếm:** Tìm theo tên môn học hoặc tên giáo viên

✅ **Xuất PDF:**
- Click "Export PDF"
- Chọn nơi lưu file
- File PDF chứa thông tin sinh viên và bảng điểm đầy đủ

✅ **Đổi mật khẩu:**
- Click "Đổi MK"
- Nhập mật khẩu cũ, mật khẩu mới, xác nhận
- Mật khẩu tối thiểu 6 ký tự

✅ **Đăng xuất:**
- Click "Logout" để quay về trang đăng nhập

---

### 5. **Demo chức năng Đăng ký**

Từ trang đăng nhập, click "Đăng Kí":

✅ **Đăng ký tài khoản mới:**
- Chọn loại tài khoản: STUDENT, TEACHER, hoặc ADMIN
- Nhập Username (3-20 ký tự, chữ, số, gạch dưới)
- Nhập Password (tối thiểu 6 ký tự)
- Xác nhận password
- Click "Đăng Kí"

⚠️ **Lưu ý:** 
- Username phải chưa tồn tại
- Sau khi đăng ký, cần Admin thêm thông tin chi tiết (sinh viên/giáo viên) vào hệ thống

---

### 6. **Demo chức năng Quên mật khẩu**

Từ trang đăng nhập, click "Quên mật khẩu?":

✅ **Đặt lại mật khẩu:**
- Nhập Username
- Nhập mật khẩu mới
- Xác nhận mật khẩu mới
- Click "Đặt lại"

---

## 🎯 Kịch bản demo đề xuất:

### **Kịch bản 1: Quy trình đầy đủ**
1. Đăng nhập Admin → Thêm 1 sinh viên mới
2. Đăng nhập Teacher → Nhập điểm cho sinh viên đó
3. Đăng nhập bằng tài khoản sinh viên → Xem điểm và xuất PDF

### **Kịch bản 2: Quản lý giáo viên**
1. Đăng nhập Admin → Thêm giáo viên mới với 2-3 môn học
2. Đăng nhập bằng tài khoản giáo viên mới → Nhập điểm cho nhiều sinh viên
3. Xuất báo cáo theo môn học

### **Kịch bản 3: Sinh viên xem điểm**
1. Đăng nhập Sinh viên → Xem bảng điểm
2. Lọc điểm theo loại (A, B, C...)
3. Tìm kiếm môn học cụ thể
4. Xuất PDF bảng điểm

---

## ⚠️ Lưu ý khi demo:

1. **Database phải đang chạy:**
   - MySQL server phải được khởi động
   - Database `qlsv_diem` phải đã được tạo (chạy `schema.sql` và `data.sql`)

2. **Cấu hình kết nối:**
   - Kiểm tra file `src/config/DBConnection.java`
   - Đảm bảo URL, USER, PASSWORD đúng với MySQL của bạn

3. **Nếu gặp lỗi kết nối:**
   - Kiểm tra MySQL đang chạy
   - Kiểm tra database đã được tạo chưa
   - Kiểm tra thông tin đăng nhập trong DBConnection.java

---

## 🚀 Cách chạy lại ứng dụng:

```powershell
# Biên dịch
cd D:\JavaFinal\Java_Final\Final
javac -cp "lib\mysql-connector-j-9.5.0.jar" -d out -encoding UTF-8 src\Main\Main.java src\config\*.java src\dao\*.java src\model\*.java src\util\*.java src\ui\*.java src\ui\admin\*.java

# Chạy ứng dụng
java -cp "out;lib\mysql-connector-j-9.5.0.jar" Main.Main
```

---

## 📊 Dữ liệu mẫu có sẵn:

- **5 giáo viên** với các môn học:
  - Lập trình Java (3 TC)
  - Cơ sở dữ liệu (3 TC)
  - Mạng máy tính (3 TC)
  - Cấu trúc dữ liệu (4 TC)
  - Hệ điều hành (3 TC)

- **15 sinh viên** (SV001 - SV015) với điểm ngẫu nhiên đã được tạo sẵn

- Bạn có thể đăng nhập và test ngay!

---

**Chúc bạn demo thành công! 🎉**

