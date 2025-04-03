
//////////////////////////////// playlist functions////////////////////////////////////////////////////////////////
function AllPlaylists() {
    if (playLists.classList.contains('d-none')) {
        playLists.classList.remove('d-none')
    }
    songsList.classList.add('d-none');
    artistsList.classList.add('d-none');
    genresList.classList.add('d-none');
    document.getElementById('playlist-songs').classList.add('d-none');
    $('#playlist-songs').html('')
    $('#artist-songs').html('')
    $('#genre-songs').html('')
}
function getMyPlaylists() {
    const userId = $('#favorite-button').data('userid');
    console.log(userId);
    $.ajax({
        url: '/TrangChu/getPlaylistByUserId?userId='+userId, // API tải danh sách bài hát yêu thích
        method: 'GET',
        dataType: 'json', // Đảm bảo nhận dữ liệu JSON
        success: function (data) {
            console.log(data); // Ghi log dữ liệu để kiểm tra
            var index = 0;
            let songsHtml =
                ``;
            // console.log(data); // Ghi log dữ liệu để kiểm tra
            data.forEach(function (playlist) {
                index++;
                songsHtml += `
                <div class="row mb-1">
                        <div class="d-flex align-items-center rounded-3 shadow-sm btn btn-light"
                        onclick="togglePlaylist('${index}','${playlist.playlistId}')"
                        style="width: 100%; height: 50px;">
                            <img src="${playlist.playlistPicUrl}" class="rounded-3" alt="Ảnh bìa" width="50px" height="50px">
                            <h6 class="card-title text-ellipsis">${playlist.playlistName}</h6>
                            ${playlist.totalTracks}<img src="/img/song-number.png" class="rounded-3" alt="Ảnh bìa" width="20px" height="20px">
                        </div>
                </div>
                <div id="my-playlist-number-${index}"></div>
            `;
            });
            // Nếu không có bài hát, hiển thị thông báo
            if (!songsHtml) {
                songsHtml = '<p class="text-white">Bạn chưa có playlist nào.</p>';
            }
            $('#my-playlist').html(songsHtml); // Hiển thị danh sách bài hát
        },
        error: function (xhr, status, error) {
            console.error('Lỗi khi tải bài hát:', xhr.responseText || status, error);
            $('#my-playlist').html('<p>Không thể tải danh sách bài hát. Vui lòng thử lại sau.</p>');
        }
    });
}
function togglePlaylist(index, playlistId) {
    const playlistElement = document.getElementById('my-playlist-number-' + index);
    // Kiểm tra trạng thái hiển thị hiện tại
    if (playlistElement.style.display === 'none' || !playlistElement.style.display) {
        playlistElement.style.display = 'block'; // Hiển thị phần tử
        getMyPlaylistSongs(index, playlistElement.dataset.playlistId); // Gọi hàm tải bài hát
    } else {
        playlistElement.style.display = 'none'; // Ẩn phần tử
    }
    getMyPlaylistSongs(index, playlistId); // Gọi hàm tải bài hát
}
function togglePlaylists() {
    const playlistElement = document.getElementById('my-playlist');
    // Kiểm tra trạng thái hiển thị hiện tại
    if (playlistElement.style.display === 'none' || !playlistElement.style.display) {
        playlistElement.style.display = 'block'; // Hiển thị phần tử
        getMyPlaylists()
    } else {
        playlistElement.style.display = 'none'; // Ẩn phần tử
    }
}
function getMyPlaylistSongs(index,playlistId) {
    $.ajax({
        url: '/TrangChu/getPlaylistSongs?playlistId='+playlistId,// API tải danh sách bài hát yêu thích
        method: 'GET',
        dataType: 'json', // Đảm bảo nhận dữ liệu JSON
        success: function (data) {
            let songsHtml =
                ``;
            // console.log(data); // Ghi log dữ liệu để kiểm tra
            data.forEach(function (song) {
                songsHtml += `
                <div class="row mb-1" id="card-title">
                        <div class="d-flex align-items-center bg-secondary rounded-3 shadow-sm" style="width: 100%; height: 50px;">
                            <img src="${song.songPicUrl}" class="rounded-3" alt="Ảnh bìa" width="50px" height="50px">
                            <h6 class="card-title play-song ms-3 text-white text-ellipsis" style=" cursor: pointer; overflow: hidden" data-songid="${song.songId}" data-date="${song.releaseDate}" data-genre="${song.genre}" data-album="${song.album}" data-songpicurl="${song.songPicUrl}" data-artist="${song.artist}" data-url="${song.songUrl}" data-songname="${song.songName}">${song.songName}</h6>
                            <img src="/img/delete.png" onclick="removeSongFromPlaylist('${index}','${playlistId}','${song.songId}')" class="delete-icon ms-3" alt="xóa" width="30px" height="30px">
                        </div>
                </div>
            `;
            });
            // Nếu không có bài hát, hiển thị thông báo
            if (!songsHtml) {
                songsHtml = '<p>Không có bài hát nào trong danh sách.</p>';
            }
            let playlist= '#my-playlist-number-'+ index
            $(playlist).html(songsHtml); // Hiển thị danh sách bài hát
            setupPlayButtons();
        },
        error: function (xhr, status, error) {
            console.error('Lỗi khi tải bài hát:', xhr.responseText || status, error);
            $('#favorite-songs').html('<p>Không thể tải danh sách bài hát. Vui lòng thử lại sau.</p>');
        }
    });
}
function showMyPlaylistToAddSong() {
    const songId = $('#song-info').find('h6').data('songid');
    const userId = $('#favorite-button').data('userid'); // ID người dùng
    $.ajax({
        url: '/TrangChu/getPlaylistByUserIdAndSongId?userId='+userId+'&songId='+songId, // API tải danh sách bài hát yêu thích
        method: 'GET',
        dataType: 'json', // Đảm bảo nhận dữ liệu JSON
        success: function (data) {
            console.log(data); // Ghi log dữ liệu để kiểm tra
            var index = 0;
            let songsHtml =
                ``;
            // console.log(data); // Ghi log dữ liệu để kiểm tra
            data.forEach(function (playlist) {
                index++;
                songsHtml += `
                <div class="row mb-1">
                        <div class="d-flex align-items-center rounded-3 shadow-sm btn btn-light" style="width: 100%; height: 50px;">
                            <img src="${playlist.playlistPicUrl}" class="rounded-3" alt="Ảnh bìa" width="50px" height="50px">
                            <h6 class="card-title text-ellipsis">${playlist.playlistName}</h6>
                            <img style="cursor: pointer;" onclick="addSongToPlaylist(${index},'${playlist.playlistId}', '${songId}')"
                            id="add-to-playlist-button" src="img/add-to-playlist.png" 
                            alt="addtoplaylist" class="ms-1" width="35px" height="35px">
                        </div>
                </div>
                <div id="my-playlist-number-${index}"></div>
            `;
            });

            // Nếu không có bài hát, hiển thị thông báo
            if (!songsHtml) {
                songsHtml = '<p class="text-white">Bài hát đã có trong tất cả các playlist của bạn.</p>';
            }
            $('#my-playlist').html(songsHtml);
            const playlistElement = document.getElementById('my-playlist');
            // Kiểm tra trạng thái hiển thị hiện tại
            if (playlistElement.style.display === 'none' || !playlistElement.style.display) {
                playlistElement.style.display = 'block'; // Hiển thị phần tử
            }
        },
        error: function (xhr, status, error) {
            console.error('Lỗi khi tải bài hát:', xhr.responseText || status, error);
            $('#my-playlist').html('<p>Không thể tải danh sách bài hát. Vui lòng thử lại sau.</p>');
        }
    });
}

function addSongToPlaylist(index,playlistId, songId) {
    const userId = $('#favorite-button').data('userid');
    $.ajax({
        url: '/TrangChu/addSongToPlaylist?playlistId='+playlistId+'&songId='+songId+'&userId='+userId, // API thêm bài hát vào playlist
        method: 'GET',
        dataType: 'json', // Đảm bảo nhận dữ liệu JSON
        success: function (data) {
            console.log(data); // Ghi log dữ liệu để kiểm tra
            getMyPlaylists()
        },
        error: function (xhr, status, error) {
            console.error('Lỗi khi thêm bài hát vào playlist:', xhr.responseText || status, error);
        }
    });
}
function removeSongFromPlaylist(index,playlistId, songId) {
    $.ajax({
        url: '/TrangChu/removeSongFromPlaylist?playlistId='+playlistId+'&songId='+songId, // API thêm bài hát vào playlist
        method: 'GET',
        dataType: 'json', // Đảm bảo nhận dữ liệu JSON
        success: function (data) {
            console.log(data); // Ghi log dữ liệu để kiểm tra
            getMyPlaylistSongs(index,playlistId)
        },
        error: function (xhr, status, error) {
            console.error('Lỗi khi xóa bài hát khỏi playlist:', xhr.responseText || status, error);
        }
    });
}
document.getElementById('my-playlist').addEventListener('shown.bs.collapse', function () {
    getMyPlaylists(); // Gọi hàm để lấy danh sách yêu thích
});

function createPlaylist() {
    console.log('create playlist');
    const userId = $('#favorite-button').data('userid');
    $.ajax({
        url: '/TrangChu/createPlaylist?userId='+ userId, // API tạo playlist
        method: 'GET',
        dataType: 'json', // Đảm bảo nhận dữ liệu JSON
        success: function (data) {
            getMyPlaylists();
            let playlistElement = document.getElementById('my-playlist');
            let collapseInstance = new bootstrap.Collapse(playlistElement, { toggle: false }); // Initialize collapse without toggling
            collapseInstance.show(); // Show the element
        },
        error: function (xhr, status, error) {
            console.error('Lỗi khi tạo playlist:', xhr.responseText || status, error);
        }
    });
}
function getAllPlaylists() {
    $.ajax({
        url: '/TrangChu/getAllPlaylists', // API tải danh sách bài hát
        method: 'GET',
        dataType: 'json', // Đảm bảo nhận dữ liệu JSON
        success: function (data) {
            let playlistsHtml = `<h4 class="w-100 text-white my-1">Danh sách playlist</h4>`;
            // console.log(data); // Ghi log dữ liệu để kiểm tra
            data.forEach(function (playlist) {
                playlistsHtml += `
                    <div class="card transparent-card no-border shadow-sm mb-5" style="width: 350px; height: 250px; ">
                    <div>
                            <img src="${playlist.playlistPicUrl}" alt="Ảnh bìa" class="card-img-top" style="position: relative; width: 100%; height: 250px; object-fit: cover;">
                            <div style="position: absolute; bottom: 0; left: 0; right: 0; padding: 10px;" >
                                    <img src="img/play-playlist.png" id="instantPlay-button" onclick="instantPlayButton('${playlist.playlistId}')" class="shadow-sm circle-button" style="cursor: pointer;" width="50px" height="50px" alt="play">
                                </div>
                            <div class="card-body text-center" id="card-title">
                                <h6 class="card-title text-white" style="cursor: pointer;" onclick="getPlaylistSongs('${playlist.playlistId}')">${playlist.playlistName}</h6>
                            </div>
                    </div>
                    </div>
                `;
            });
            // Nếu không có bài hát, hiển thị thông báo
            if (!playlistsHtml) {
                playlistsHtml = '<p>Không có bài hát nào trong danh sách.</p>';
            }
            $('#playlists-list').html(playlistsHtml); // Hiển thị danh sách bài hát
        }
    });
}
function instantPlayButton(playlistId) {
    const url = '/TrangChu/getPlaylistSongs?playlistId='+playlistId;
    $.ajax({
        url: url, // API tải danh sách bài hát
        method: 'GET',
        dataType: 'json', // Đảm bảo nhận dữ liệu JSON
        success: function (playlist) {
            console.log(playlist);
            playSongFromPlaylist(0, playlist)
            setupPlaylistButtons(playlist)
        }
    });
}
function getPlaylistSongs(playlistId) {
    scrollToTop();
    songsList.classList.add('d-none');
    playLists.classList.add('d-none');
    artistsList.classList.add('d-none');
    genresList.classList.add('d-none');
    document.getElementById('playlist-songs').classList.remove('d-none');
    // get playlist info
    const url1 = '/TrangChu/getPlaylistById?playlistId='+playlistId;
    $.ajax({
        url: url1, // API tải danh sách bài hát
        method: 'GET',
        dataType: 'json', // Đảm bảo nhận dữ liệu JSON
        success: function (playlist) {
            let playlistSongsHtml = `
                <div class="container">
                    <div class="">
                        <div>
                            <img src="${playlist.playlistPicUrl}" alt="Ảnh bìa" class="border border-3 border-white rounded-3" style="width: 100%; height: 250px; object-fit: cover;">
                        </div>
                        <div class="d-flex flex-column justify-content-center m-3">
                            <!-- Playlist Name -->
                            <div class="d-flex justify-content-center align-items-center">
                                <h5 class="mx-3 text-white">${playlist.playlistName}</h5>
                            </div>
                            <div class="d-flex align-items-center text-white justify-content-center">
                                <h5 class="mx-1">Số bài hát trong playlist:</h5>
                                <h5 >${playlist.totalTracks}</h5>
                            </div>
                        </div>
                    </div>
                    <div class="d-flex flex-wrap" id="playlist-songs-body"></div>
                </div>
            `;
            $('#playlist-songs').html(playlistSongsHtml); // Hiển thị danh sách bài hát
        },
        error: function (xhr, status, error) {
            console.error('Lỗi khi tải bài hát:', xhr.responseText || status, error);
            $('#playlist-songs').html('<p>Không thể tải danh sách phát. Vui lòng thử lại sau.</p>');
        }
    });
    // get playlist songs
    const url2 = '/TrangChu/getPlaylistSongs?playlistId='+playlistId;
    $.ajax({
        url: url2, // API tải danh sách bài hát
        method: 'GET',
        dataType: 'json', // Đảm bảo nhận dữ liệu JSON
        success: function (playlist) {
            var index = -1;
            let songsHtml = ``;
            playlist.forEach(function (song) {
                index++;
                songsHtml += `
                <div class="row m-1">
                        <div class="col" style="width: 250px">
                            <div class="card transparent-card no-border shadow-sm">
                                <img src="${song.songPicUrl}" class="card-img-top" alt="Ảnh bìa">
                                <div class="card-body text-center" id="card-title">
                                <h6 class="card-title text-white playlist-song" style=" cursor: pointer;" data-index="${index}" data-songid="${song.songId}" data-date="${song.releaseDate}" data-genre="${song.genre}" data-album="${song.album}" data-songpicurl="${song.songPicUrl}" data-artist="${song.artist}" data-url="${song.songUrl}" data-songname="${song.songName}">${song.songName}</h6>
                                </div>
                            </div>
                        </div>
                    </div>
                `
                $('#playlist-songs-body').html(songsHtml); // Hiển thị danh sách bài hát
            });
            setupPlaylistButtons(playlist);
        },
        error: function (xhr, status, error) {
            console.error('Lỗi khi tải bài hát:', xhr.responseText || status, error);
            $('#playlist-songs').html('<p>Không thể tải danh sách phát. Vui lòng thử lại sau.</p>');
        }
    });
}
function setupPlaylistButtons(playlist) {
    let currentSongIndex = 0; // Chỉ mục bài hát hiện tại

    // Xử lý sự kiện click để phát bài hát từ playlist
    $('.playlist-song').off('click').on('click', function () {
        currentSongIndex = $(this).data('index'); // Lấy chỉ mục từ nút
        playSongFromPlaylist(currentSongIndex, playlist);
    });
    // Xử lý sự kiện click cho nút shuffle

    // Xử lý sự kiện click cho nút prev
    $('#prev-button').off('click').on('click', function () {
        playPrevSong(playlist);
    });

    // Xử lý sự kiện click cho nút next
    $('#next-button').off('click').on('click', function () {
        playNextSong(playlist);
    });
    function playNextSong(playlist) {
        currentSongIndex++;
        if (currentSongIndex >= playlist.length) {
            currentSongIndex = 0; // Quay lại bài đầu tiên
        }
        playSongFromPlaylist(currentSongIndex, playlist);
    }

    // Hàm phát bài hát trước đó
    function playPrevSong(playlist) {
        currentSongIndex--;
        if (currentSongIndex < 0) {
            currentSongIndex = playlist.length - 1; // Quay lại bài cuối cùng
        }
        playSongFromPlaylist(currentSongIndex, playlist);
    }
}
// Hàm phát bài hát theo chỉ mục
function playSongFromPlaylist(index, playlist) {
    const song = playlist[index]; // Lấy thông tin bài hát từ playlist
    const songId = song.songId;
    const audioPlayer = $('#audio-player');

    if (!song) {
        console.error('Chỉ mục bài hát không hợp lệ:', index);
        return;
    }

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

    // Cập nhật nguồn nhạc và phát
    audioPlayer.attr('src', song.songUrl);
    try {
        audioPlayer[0].play();
    } catch (error) {
        console.error('Lỗi khi phát nhạc:', error);
    }
    $('#shuffle-button').off('click').on('click', function () {
        shuffleSong(index, playlist);
    });
    // Cập nhật giao diện bài hát
updateSongInfo(songId, song.songPicUrl, song.songName, song.artist, song.genre, song.album, song.releaseDate);
    // Cập nhật nút Play/Pause
    playPause.src = 'img/pause-button.png';
    playPause.alt = 'pause';

    // Xử lý tự động phát bài hát tiếp theo khi bài hát hiện tại kết thúc
    audioPlayer.off('ended').on('ended', function () {
        playNextSong(playlist);
    });
}
// Hàm phát bài hát ngẫu nhiên
function shuffleSong(currentSongIndex, playlist) {
    // Chọn một chỉ mục ngẫu nhiên từ playlist
    do {
        randomIndex = Math.floor(Math.random() * playlist.length); // Chọn một chỉ mục ngẫu nhiên
    } while (randomIndex === currentSongIndex);


    // Phát bài hát ngẫu nhiên
    playSongFromPlaylist(randomIndex, playlist);
}
