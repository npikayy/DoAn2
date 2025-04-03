const songsList = document.getElementById('songs-list');
const playLists = document.getElementById('playlists-list');
const artistsList = document.getElementById('artists-list');
const genresList = document.getElementById('genres-list');
const favoriteSongBtn = document.getElementById('favorite-button');
const addToPlaylisdBtn = document.getElementById('add-to-playlist-button');
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
function scrollToTop() {
    window.scrollTo({
        top: 0, // Vị trí đầu trang
        behavior: 'smooth' // Hiệu ứng cuộn mượt mà
    });


}
function AllSongs() {
        if (songsList.classList.contains('d-none')){
            songsList.classList.remove('d-none')
        }
    playLists.classList.add('d-none');
    artistsList.classList.add('d-none');
    genresList.classList.add('d-none');
    document.getElementById('playlist-songs').classList.add('d-none');
    $('#playlist-songs').html('')
    $('#artist-songs').html('')
    $('#genre-songs').html('')
}
function getAllFavoriteSongs() {
    const userId = $('#favorite-button').data('userid');
    $.ajax({
        url: '/TrangChu/getFavoriteSongs?userId='+userId, // API tải danh sách bài hát yêu thích
        method: 'GET',
        dataType: 'json', // Đảm bảo nhận dữ liệu JSON
        success: function (data) {
            let songsHtml =
                ``;
            // console.log(data); // Ghi log dữ liệu để kiểm tra
            data.forEach(function (song) {
                songsHtml += `
                <div class="row mb-1" id="card-title">
                        <div class="d-flex align-items-center bg-light rounded-3 shadow-sm" style="width: 100%; height: 50px;">
                            <img src="${song.songPicUrl}" class="rounded-3" alt="Ảnh bìa" width="50px" height="50px">
                            <h6 class="card-title play-song ms-3 text-ellipsis" style=" cursor: pointer; overflow: hidden" data-songid="${song.songId}" data-date="${song.releaseDate}" data-genre="${song.genre}" data-album="${song.album}" data-songpicurl="${song.songPicUrl}" data-artist="${song.artist}" data-url="${song.songUrl}" data-songname="${song.songName}">${song.songName}</h6>
                        </div>
                </div>
            `;
            });
            // Nếu không có bài hát, hiển thị thông báo
            if (!songsHtml) {
                songsHtml = '<p>Không có bài hát nào trong danh sách.</p>';
            }
            $('#favorite-songs').html(songsHtml); // Hiển thị danh sách bài hát
            setupPlayButtons();
        },
        error: function (xhr, status, error) {
            console.error('Lỗi khi tải bài hát:', xhr.responseText || status, error);
            $('#favorite-songs').html('<p>Không thể tải danh sách bài hát. Vui lòng thử lại sau.</p>');
        }
    });
}

document.getElementById('favorite-songs').addEventListener('shown.bs.collapse', function () {
    getAllFavoriteSongs(); // Gọi hàm để lấy danh sách yêu thích
});

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
                                <h6 class="card-title text-white play-song" style=" cursor: pointer;" data-songid="${song.songId}" data-date="${song.releaseDate}" data-genre="${song.genre}" data-album="${song.album}" data-songpicurl="${song.songPicUrl}" data-artist="${song.artist}" data-url="${song.songUrl}" data-songname="${song.songName}">${song.songName}</h6>
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
        const songId = $(this).data('songid'); // ID bài hát
        const songName = $(this).data('songname'); // Tên bài hát
        const artistName = $(this).data('artist'); // Tên ca sĩ
        const songPicUrl = $(this).data('songpicurl');
        const genre = $(this).data('genre'); // Thể loại
        const album = $(this).data('album'); // Album
        const releaseDate = $(this).data('date'); // Ngày phát hành

        const userId = $('#favorite-button').data('userid'); // ID người dùng
        let url = '/TrangChu/getFavoriteSongIds?userId='+userId
        $.ajax({
            url: url,
            method: 'Get',
            success: function (data) {
                if (data.includes(songId)) {
                    favoriteSongBtn.src = 'img/favorited.png'; // Đổi hình ảnh sang nút yêu thích
                }else{
                    favoriteSongBtn.src = 'img/notFavorited.png';
                }
            }
        });
        $('#favorite-button').off('click').on('click', function () {
            let url1 = '/TrangChu/addFavoriteSong?userId=' + userId + '&songId=' + songId;
            let url2 = '/TrangChu/removeFavoriteSong?userId=' + userId + '&songId=' + songId;

            $.ajax({
                url: url1,
                method: 'Get',
                success: function (data) {
                    if (data == '1') {
                        favoriteSongBtn.src = 'img/favorited.png'; // Đổi hình ảnh sang trạng thái yêu thích
                        $('#favorite-songs').html("")
                        getAllFavoriteSongs();
                    } else {
                        favoriteSongBtn.src = 'img/notFavorited.png';
                        $.ajax({
                            url: url2,
                            method: 'Get',
                            success: function () {
                                $('#favorite-songs').html("")
                                getAllFavoriteSongs();
                            },
                            error: function (xhr, status, error) {
                                console.error('Lỗi khi xóa bài hát:', xhr.responseText || status, error);
                            }
                        });
                    }

                    // Gọi hàm getAllFavoriteSongs() để làm mới danh sách yêu thích

                },
                error: function (xhr, status, error) {
                    console.error('Lỗi khi thêm bài hát vào danh sách yêu thích:', xhr.responseText || status, error);
                }
            });
        });
        const audioPlayer = $('#audio-player'); // Trình phát nhạc
        // Cập nhật trình phát nhạc
        audioPlayer.attr('src', songUrl); // Cập nhật bài hát
        audioPlayer[0].play(); // Phát nhạc
        // Cập nhật nút Play/Pause
        playPause.src = 'img/pause-button.png'; // Đổi hình ảnh sang nút tạm dừng
        playPause.alt = 'pause';
        updateSongInfo(songId,songPicUrl, songName, artistName, genre, album, releaseDate)
    });
}
function updateSongInfo(songId,songPicUrl, songName, artistName, genre, album, releaseDate) {
    // Cập nhật ảnh đại diện bài hát
    $('#song-info img[alt="Logo"]').attr('src', songPicUrl);

    // Cập nhật thông tin tên bài hát và ca sĩ
    $('#song-info div').html(`
        <h6 style="color: #000000; cursor: pointer;" class="text-ellipsis" data-songid="${songId}" data-genre="${genre}" data-album="${album}" data-date="${releaseDate}" onclick="showSongDetails('${songName}', '${artistName}', '${songPicUrl}', '${genre}', '${album}', '${releaseDate}')">${songName}</h6>
        <p style="color: #000000;" class="text-ellipsis">${artistName}</p>
    `);

}
function showSongDetails(songName, artistName, songPicUrl, genre, album, releaseDate) {
    // Xóa khung chi tiết nếu đã tồn tại
    $('#song-details').remove();
    // Tạo khung thông tin
    const songDetailsHtml = `
        <div id="song-details" style="position: absolute; bottom: 10%; left: 10%; transform: translateX(-50%); background: rgba(0, 0, 0, 0.8); color: #fff; padding: 15px; border-radius: 10px; z-index: 10; max-width: 400px;">
            <div style="text-align: center;">
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
        const songId = $('#play-song').data('songid'); // ID bài hát
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
                            <div class="border-bottom w-100 pb-2 mb-2 btn btn-light d-flex flex-column align-items-start">
                                <h6 class="card-title text-dark play-song" style=" cursor: pointer;" data-songid="${song.songId}" data-date="${song.releaseDate}" data-genre="${song.genre}" data-album="${song.album}" data-songpicurl="${song.songPicUrl}" data-artist="${song.artist}" data-url="${song.songUrl}" data-songname="${song.songName}">${song.songName}</h6>
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