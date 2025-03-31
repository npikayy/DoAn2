
//////////////////////////////// playlist functions////////////////////////////////////////////////////////////////
function AllPlaylists() {
    if (playLists.classList.contains('d-none')) {
        playLists.classList.remove('d-none')
    }
    songsList.classList.add('d-none');
    artistsList.classList.add('d-none');
    genresList.classList.add('d-none');
    document.getElementById('artist-songs-title').classList.add('d-none');
    $('#playlist-songs').html('')
    $('#artist-songs-tbody').html('')
    $('#genre-songs-tbody').html('')
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
                    <div class="card transparent-card no-border shadow-sm mb-5" style="width: 200px; height: 250px; ">
                    <div style="">
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
    songsList.classList.add('d-none');
    playLists.classList.add('d-none');
    artistsList.classList.add('d-none');
    genresList.classList.add('d-none');
    document.getElementById('artist-songs-title').classList.add('d-none');
    // get playlist info
    const url1 = '/TrangChu/getPlaylistById?playlistId='+playlistId;
    $.ajax({
        url: url1, // API tải danh sách bài hát
        method: 'GET',
        dataType: 'json', // Đảm bảo nhận dữ liệu JSON
        success: function (playlist) {
            let playlistSongsHtml = `
                <div class="container">
                    <div>
                        <div>
                            <img src="${playlist.playlistPicUrl}" alt="Ảnh bìa" class="" style="width: 100%; height: 250px; object-fit: cover;">
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
                    <div class="table-container">
                    <table class="table table-bordered transparent-card">
                        <tbody id="playlist-songs-tbody"></tbody>
                    </table>
                    </div>
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
            playlist.forEach(function (song) {
                index++;
                let songHtml = `
                <tr>
                    <td><img src="${song.songPicUrl}" alt="${song.songName}" style="width: 100%; height: 50px; object-fit: cover;"></td>
                    <td class="text-white">${song.songName}</td>
                    <td class="text-white">${song.artist}</td>
                    <td>
                        <button class="btn btn-primary playlist-song rounded-pill" data-index="${index}" data-songpicurl="${song.songPicUrl}" data-artist="${song.artist}" data-url="${song.songUrl}" data-songname="${song.songName}" >Phát</button>
                    </td>
                </tr>
                `
                $('#playlist-songs-tbody').append(songHtml); // Hiển thị danh sách bài hát
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
    const audioPlayer = $('#audio-player');

    if (!song) {
        console.error('Chỉ mục bài hát không hợp lệ:', index);
        return;
    }

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
    $('#song-info').html(`
            <img src="${song.songPicUrl}" alt="${song.songName}" class="rounded-circle me-2" width="60" height="60">
            <div style="height: 100px; padding-top: 10px;">
                <h7 style="color: #ffffff; height: 100px;">${song.songName}</h7>
                <p style="color: #e7e7e7;">${song.artist}</p>
            </div>
            <img src="img/notFavorited.png" alt="comment" class="mb-2" width="35px" height="35px">
        `);

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
