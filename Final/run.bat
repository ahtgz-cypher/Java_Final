@echo off
chcp 65001 >nul
echo ========================================
echo   HỆ THỐNG QUẢN LÝ ĐIỂM SINH VIÊN
echo ========================================
echo.

REM Kiểm tra thư mục out
if not exist "out" (
    echo [INFO] Tạo thư mục out...
    mkdir out
)

REM Biên dịch
echo [INFO] Đang biên dịch dự án...
javac -cp "lib\mysql-connector-j-9.5.0.jar" -d out -encoding UTF-8 src\Main\Main.java src\config\*.java src\dao\*.java src\model\*.java src\util\*.java src\ui\*.java src\ui\admin\*.java

if %ERRORLEVEL% NEQ 0 (
    echo [ERROR] Biên dịch thất bại! Vui lòng kiểm tra lại.
    pause
    exit /b 1
)

echo [SUCCESS] Biên dịch thành công!
echo.
echo [INFO] Đang khởi động ứng dụng...
echo.

REM Chạy ứng dụng
java -cp "out;lib\mysql-connector-j-9.5.0.jar" Main.Main

if %ERRORLEVEL% NEQ 0 (
    echo.
    echo [ERROR] Ứng dụng gặp lỗi!
    echo.
    echo [HINT] Kiểm tra:
    echo   1. MySQL server đang chạy
    echo   2. Database qlsv_diem đã được tạo
    echo   3. Thông tin kết nối trong DBConnection.java
    echo.
    pause
)

