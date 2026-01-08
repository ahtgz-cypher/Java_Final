@echo off
chcp 65001 >nul
echo ========================================
echo   BIÊN DỊCH DỰ ÁN
echo ========================================
echo.

REM Tạo thư mục out nếu chưa có
if not exist "out" (
    echo [INFO] Tạo thư mục out...
    mkdir out
) else (
    echo [INFO] Xóa các file class cũ...
    del /Q out\*.* >nul 2>&1
)

REM Biên dịch
echo [INFO] Đang biên dịch...
javac -cp "lib\mysql-connector-j-9.5.0.jar" -d out -encoding UTF-8 src\Main\Main.java src\config\*.java src\dao\*.java src\model\*.java src\util\*.java src\ui\*.java src\ui\admin\*.java

if %ERRORLEVEL% EQU 0 (
    echo.
    echo [SUCCESS] Biên dịch thành công!
    echo [INFO] Các file class đã được lưu trong thư mục: out\
) else (
    echo.
    echo [ERROR] Biên dịch thất bại!
    echo [HINT] Kiểm tra:
    echo   - Java JDK đã được cài đặt
    echo   - File mysql-connector-j-9.5.0.jar có trong thư mục lib\
    echo   - Các file .java không có lỗi cú pháp
)

echo.
pause

