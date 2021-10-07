# android-music-app
## Clone project về máy
- Sau khi được mời, vào gmail để chấp nhận lời mời
- Lấy [Personal Access Token](https://docs.github.com/en/github/authenticating-to-github/keeping-your-account-and-data-secure/creating-a-personal-access-token)
- clone project về: `git clone https://<username>:<personal-access-token>@github.com/ThienNam23/android-music-app.git`

## Coding - Trước khi code 1 tính năng mới
- Trên Github, tạo branch mới từ `master`
- Trên máy cũng phải checkout sang `master`
- Dùng lệnh để đồng bộ git từ Github về máy: `git pull`
- Checkout sang branch vừa tạo
- Viết code
> Không được code vào branch `master`

## Commit - Đẩy code lên Github
- Dùng lệnh để đẩy code lên Github
- Vào Github tạo Pull Request (có ảnh minh hoạ)
	- Có thể viết mô tả hoặc không
	- Thêm Reviewer là `ThienNam23` và Assignee là acc của mình
	- Tạo Pull Request
![image](https://user-images.githubusercontent.com/57934392/133895470-e5ba18f8-0ea5-470e-8b4f-dd82b96eb150.png)

# Project - App nghe nhạc offline - online

## Giới thiệu
App nghe nhạc có 2 phần nghe offline và online. App có các tính năng:

### Nghe offline:
- Tự động tìm kiếm các bài hát trong bộ nhớ
- Tạo playlist riêng
- Thay đổi ảnh bài hát, ảnh playlist
- Thêm lời bài hát
> Mở rộng:
- Phát theo độ yêu thích (tính theo số lần nghe)
- Cắt nhạc 
- Cài đặt nhạc làm nhạc chuông
- Đặt thời gian phát
- More...

### Nghe online:
- Tạo playlist riêng
- Tải nhạc xuống
- Nghe theo album, thể loại, ca sĩ
> Mở rộng: (một số phần mở rộng yêu cầu hệ thống quản lý tài khoản - hệ thống này nếu có thời gian mới xây dựng được)
- Sau khi nhạc được tải xuống thì được áp dụng phần mở rộng của offline
- Chế độ yêu thích (yêu cầu đăng nhập)
- Chế độ bình luận (yêu cầu đăng nhập)

## Xây dựng app:
### ERD và các mẫu UI:
- Tải [file](https://drive.google.com/file/d/1Wj4lWKbS2HyE98klMUWq-Q9F-ntVWGdi/view?usp=sharing) này về, up lên drive
- Vào [Draw.io](https://draw.io) đăng nhập với gmail, và cho phép [Draw.io](https://draw.io) quyền đọc ghi trên drive để mở file này lên
