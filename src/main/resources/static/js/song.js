const songsList = document.getElementById('songs-list');
const playLists = document.getElementById('playlists-list');
const artistsList = document.getElementById('artists-list');
const genresList = document.getElementById('genres-list');
const progressBar = document.getElementById('progress-bar');
const currentTimeDisplay = document.getElementById('current-time');
const totalTimeDisplay = document.getElementById('total-time');
const playPause = document.getElementById('play-pause-button');
const prevButton = document.getElementById('prev-button');
const prevImg = prevButton.querySelector('img');
const nextButton = document.getElementById('next-button');
const nextImg = nextButton.querySelector('img');
const loopImg = document.getElementById('loop-button');
const audioPlayer = document.getElementById("audio-player");
const volumeButton = document.getElementById('volume-button');
const volumeBar = document.getElementById('volume-bar');
// SONG SECTION USING AJAX AND JQUERY//

/////////////////////////////////////////////// song functions/////////////////////////////////////////////////
function AllSongs() {
        if (songsList.classList.contains('d-none')){
            songsList.classList.remove('d-none')
        }
    playLists.classList.add('d-none');
    artistsList.classList.add('d-none');
    genresList.classList.add('d-none');
    document.getElementById('artist-songs-title').classList.add('d-none');
    $('#playlist-songs').html('')
    $('#artist-songs-tbody').html('')
    $('#genre-songs-tbody').html('')
}
function getAllSongs() {
        $.ajax({
            url: '/TrangChu/getAllSongs', // API tải danh sách bài hát
            method: 'GET',
            dataType: 'json', // Đảm bảo nhận dữ liệu JSON
            success: function (data) {
                let songsHtml =
                    `<h4 class="w-100 text-white mb-3">Danh sách bài hát gợi ý cho bạn</h4>`;
                // console.log(data); // Ghi log dữ liệu để kiểm tra
                data.forEach(function (song) {
                    songsHtml += `
                    <div class="row m-1">
                        <div class="col" style="width: 200px">
                            <div class="card transparent-card no-border shadow-sm">
                                <img src="${song.songPicUrl}" class="card-img-top" alt="Ảnh bìa">
                                <div class="card-body text-center" id="card-title">
                                <h6 class="card-title text-white play-song" style=" cursor: pointer;" data-date="${song.releaseDate}" data-genre="${song.genre}" data-album="${song.album}" data-songpicurl="${song.songPicUrl}" data-artist="${song.artist}" data-url="${song.songUrl}" data-songname="${song.songName}">${song.songName}</h6>
                                <p class="card-text"></p>
                                </div>
                            </div>
                        </div>
                    </div>
                `;
                });
                // Nếu không có bài hát, hiển thị thông báo
                if (!songsHtml) {
                    songsHtml = '<p>Không có bài hát nào trong danh sách.</p>';
                }
                $('#songs-list').html(songsHtml); // Hiển thị danh sách bài hát
                setupPlayButtons();
            },
            error: function (xhr, status, error) {
                console.error('Lỗi khi tải bài hát:', xhr.responseText || status, error);
                $('#songs-list').html('<p>Không thể tải danh sách bài hát. Vui lòng thử lại sau.</p>');
            }
        });
}
$('#loop-button').on('click', function () {
    // Kiểm tra trạng thái hiện tại trong session storage
    const isLooping = localStorage.getItem('loop') === 'true';

    // Đảo ngược trạng thái loop
    if (isLooping) {
        this.src = 'img/not-loop.png'; // Đổi biểu tượng loop
        this.alt = 'loop off';
        localStorage.setItem('loop', 'false'); // Tắt lặp
        this.classList.remove('activated');
    } else {
        localStorage.setItem('loop', 'true'); // Bật lặp
        this.src = 'img/loop-one.png'; // Đổi biểu tượng loop
        this.alt = 'loop on';
        this.classList.add('activated'); // Thêm class active cho nút
    }
});
function setupPlayButtons() {
    $('.play-song').off('click').on('click', function () {
        const songUrl = $(this).data('url'); // URL bài hát
        const songName = $(this).data('songname'); // Tên bài hát
        const artistName = $(this).data('artist'); // Tên ca sĩ
        const songPicUrl = $(this).data('songpicurl');
        const genre = $(this).data('genre'); // Thể loại
        const album = $(this).data('album'); // Album
        const releaseDate = $(this).data('date'); // Ngày phát hành

        const audioPlayer = $('#audio-player'); // Trình phát nhạc
        // Cập nhật trình phát nhạc
        audioPlayer.attr('src', songUrl); // Cập nhật bài hát
        audioPlayer[0].play(); // Phát nhạc
        // Cập nhật nút Play/Pause
        playPause.src = 'img/pause-button.png'; // Đổi hình ảnh sang nút tạm dừng
        playPause.alt = 'pause';
        $('#song-info').html(`
            <img src="${songPicUrl}" alt="${songName}" class="rounded-circle me-2" width="60" height="60">
            <div style="height: 100px; padding-top: 10px;">
                <h7 style="color: #ffffff; height: 100px; cursor: pointer;" onclick="showSongDetails('${songName}', '${artistName}', '${songPicUrl}', '${genre}', '${album}', '${releaseDate}')">${songName}</h7>
                <p style="color: #e7e7e7;">${artistName}</p>
            </div>
            <img src="img/notFavorited.png" alt="comment" class="mb-2" width="35px" height="35px">
        `);

    });
}
function showSongDetails(songName, artistName, songPicUrl, genre, album, releaseDate) {
    // Xóa khung chi tiết nếu đã tồn tại
    $('#song-details').remove();
    // Tạo khung thông tin
    const songDetailsHtml = `
        <div id="song-details" style="position: absolute; bottom: 10%; left: 10%; transform: translateX(-50%); background: rgba(0, 0, 0, 0.8); color: #fff; padding: 15px; border-radius: 10px; z-index: 10; max-width: 400px;">
            <div style="text-align: center;">
                <img src="${songPicUrl}" alt="${songName}" style="width: 60px; height: 60px; border-radius: 50%; margin-bottom: 10px;">
                <h4>${songName}</h4>
                <p>Ca sĩ: ${artistName}</p>
                <p>Album: ${album}</p>
                <p>Ngày phát hành: ${releaseDate}</p>
                <p>Thể loại: ${genre}</p>
                <button onclick="hideSongDetails()" style="background: red; color: white; border: none; padding: 5px 10px; border-radius: 5px; cursor: pointer;">Đóng</button>
            </div>
        </div>
    `;

    // Thêm khung thông tin vào DOM
    $('.player').append(songDetailsHtml);
}

// Hàm đóng khung thông tin
function hideSongDetails() {
    $('#song-details').remove();
}
    // Lắng nghe sự kiện 'submit' của form
$('#search-input').on('keyup', function (e) {
        e.preventDefault(); // Ngăn gửi biểu mẫu mặc định
        const searchResults = document.getElementById('search-results');
        // Lấy giá trị từ ô nhập liệu
        const songName = document.getElementById('search-input').value

        // Kiểm tra giá trị ô tìm kiếm
        if (!songName) {
            searchResults.classList.add('d-none');
            return;
        }
        const url = '/TrangChu/searchSongs?songName=' + songName
        // Gửi yêu cầu tới API tìm kiếm
        $.ajax({
            url: url,
            method: 'GET',
            dataType: 'json',
            success: function (data) {
                let resultsHtml = `<div class="bg-light p-3 rounded shadow-sm">
                `;
                    data.forEach(function (song) {
                        resultsHtml += `
                            <div class="border-bottom pb-2 mb-2" id="search-song-title">
                                <h6 class="card-title text-dark play-song" style=" cursor: pointer;" data-date="${song.releaseDate}" data-genre="${song.genre}" data-album="${song.album}" data-songpicurl="${song.songPicUrl}" data-artist="${song.artist}" data-url="${song.songUrl}" data-songname="${song.songName}">${song.songName}</h6>
                                <small class="text-muted">Nghệ sĩ: ${song.artist}</small>
                            </div>
                        `;

                    });

                    if (data.length === 0) {
                        resultsHtml = '<p class="text-muted">Không tìm thấy bài hát phù hợp.</p>';
                    }
                resultsHtml += `</div>`;
                $('#search-results').html(resultsHtml); // Hiển thị kết quả
                searchResults.classList.remove('d-none'); // Hiển thị kết quả
                setupPlayButtons()
            },
            error: function (xhr, status, error) {
                console.error('Lỗi khi tìm kiếm bài hát:', error);
                $('#search-results').html('<p class="text-danger">Có lỗi xảy ra khi tìm kiếm. Vui lòng thử lại sau.</p>');
            }
        });
    });