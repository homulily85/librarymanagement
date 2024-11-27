# Ứng dụng quản lí thư viện sử dụng JavaFx

## Tác giả

- Nguyễn Kim Trung Đức - 20231533
- Nguyễn Phương Linh - 23021609
- Nguyễn Quang Bảo Sơn - 23021685

## Mô tả chung

Đây là ứng dựng được xây dựng để hỗ trợ quản lí thư viện. Ứng dụng được viết bằng Java và sử dụng thư viện JavaFx kết
hợp với các thư viện khác. Ứng dụng được phân quyền cho 2 loại người dùng: người quản lí và người dùng thông thường (
thành viên).

## Các tính năng chính

- Đối tất cả người dùng
    - Đăng nhập: Người dùng có thể đăng nhập vào hệ thống với tài khoản của mình.
    - Đăng kí: Người dùng có thể đăng kí tài khoản mới.
    - Quên mật khẩu: Người dùng có thể lấy lại mật khẩu qua email.
- Đối với người quản lí
    - Quản lí sách: Người quản lí có thể tìm kiếm thêm, sửa, xóa tài liệu.
    - Quản lí người dùng: Người quản lí có thể tìm kiếm, thay đổi thông tin thành viên.
    - Quản lí mượn trả: Người quản lí có thể xem, tìm kiếm thông tin mượn trả, xác nhận yêu cầu mượn tài liệu, xác nhận
      trả
      tài liệu, hủy yêu cầu mượn tài liệu, gia han mượn tài liệu.
    - Thống kê: Người quản lí có thể xem thống kê số lượng tài liệu, số lượng thành viên, số lượng yêu cầu.
- Đối với thành viên
    - Thống kê: Thành viên có thể xem thống kê về số lượng tài liệu đã yêu cầu, số lượng tài liệu đang mượn, số lượng
      tài liệu đã quá hạn trả, biểu đề thống kê số lượng tài liệu đã mượn theo tháng...
    - Đề xuất tài liệu: Ứng dụng sẽ gợi ý cho thành viên những tài liệu mà họ có thể quan tâm dựa trên xu hướng của các
      thành viên khác và dựa trên đề xuất của người quản lí thư viện.
    - Tìm kiếm, xem thông tin, tạo yêu cầu mượn tài liệu.
    - Thêm đánh giá, bình luận cho tài liệu.
    - Xem thông tin cá nhân, đổi mật khẩu, thay đổi ảnh đại diện. Đối với các thông tin cá nhân khác, người dùng cần
      liên hệ người quản lí thư viện để thay đổi.
    - Xem thông tin về các tất cả các yêu cầu mượn tài liệu của mình.

## Cài đặt

Clone toàn bộ mã nguồn của dự án về máy tính của bạn sau đó chạy mã nguồn thông qua IDE của bạn.
Để ứng dung chạy chính xác, bạn cần thêm các lệnh sau vào VM options của IDE của bạn:

```
--add-exports
javafx.controls/com.sun.javafx.scene.control.behavior=com.jfoenix
--add-exports
javafx.controls/com.sun.javafx.scene.control=com.jfoenix
--add-exports
javafx.base/com.sun.javafx.binding=com.jfoenix
--add-exports
javafx.graphics/com.sun.javafx.stage=com.jfoenix
--add-exports
javafx.base/com.sun.javafx.event=com.jfoenix
--add-opens
java.base/java.lang.reflect=com.jfoenix
--add-exports
javafx.controls/com.sun.javafx.scene.control.behavior=com.jfoenix
```

## Thư viện sử dụng

- JavaFx: Xây dựng giao diện
- SQLITE-JDBC: Kết nối với cơ sở dữ liệu
- password4j: Băm mật khẩu
- JavaMail: Gửi email
- ControlFx: Gửi thông báo
- jfoenix: Áp dụng giao diện Material Design

## API sử dụng
- Google Books API: Lấy thông tin sách từ Google Books API

## Biểu đồ UML
![alt text](https://github.com/homulily85/librarymanagement/blob/master/uml.png)

## Cải tiến trong tương lai
- Cho phép người quản lí thư viện gửi thông báo tới tất cả các thành viên hoặc (một số) thành viên cụ thể.
- Tối ưu hóa trải nghiệm người dùng.
