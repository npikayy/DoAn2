
window.onload = function () {
    getEverything()
};

function toggleTextColorOnActive() {
    // Lấy tất cả các phần tử có class 'nav-link'
    const navLinks = document.querySelectorAll('.nav-link');

    // Lặp qua từng phần tử
    navLinks.forEach(link => {
        if (link.classList.contains('active')) {
            // Nếu có class 'active', thay đổi từ 'text-white' thành 'text-dark'
            link.classList.remove('text-white');
            link.classList.add('text-dark');
        } else {
            // Nếu không có class 'active', đảm bảo class 'text-white' được áp dụng
            link.classList.remove('text-dark');
            link.classList.add('text-white');
        }
    });
}
function redirectToLogin() {
    window.location.href = "/login";
}
document.querySelectorAll('.nav-link').forEach(link => {
    link.addEventListener('click', () => {
        toggleTextColorOnActive(); // Gọi hàm kiểm tra class active
    });
});
function getAll() {
    if (songsList.classList.contains('d-none')){
        songsList.classList.remove('d-none')
    }
    if (playLists.classList.contains('d-none')){
        playLists.classList.remove('d-none')
    }
    if (artistsList.classList.contains('d-none')){
        artistsList.classList.remove('d-none')
    }
    if (genresList.classList.contains('d-none')){
        genresList.classList.remove('d-none')
    }
    document.getElementById('playlist-songs').classList.add('d-none');
    $('#playlist-songs').html('');
    $('#artist-songs').html('')
    $('#genre-songs').html('')
}
function getEverything() {
    getAllSongs();
    getAllPlaylists();
    getAllArtists();
    getAllGenres()
    document.getElementById('playlist-songs').classList.add('d-none');
    const savedSongUrl = localStorage.getItem("currentSongUrl");
    const savedTime = localStorage.getItem("currentTime");
    const savedSongName = localStorage.getItem("currentSongName");
    const savedArtistName = localStorage.getItem("artistName");
    const savedSongPicUrl = localStorage.getItem("songPicUrl");
    const savedGenre = localStorage.getItem("genre");
    const savedAlbum = localStorage.getItem("album");
    const savedReleaseDate = localStorage.getItem("releaseDate");
    const savedVolume = localStorage.getItem("volume");
    const isLooping = localStorage.getItem('loop') === 'true';
    const songId = localStorage.getItem("songId");
    const userId = $('#favorite-button').data('userid'); // ID người dùng
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
    if (savedSongUrl) {
        // Cập nhật trình phát nhạc
        audioPlayer.src = savedSongUrl;
        audioPlayer.currentTime = savedTime ? parseFloat(savedTime) : 0;
        volumeBar.value = savedVolume ? parseFloat(savedVolume) : 1;
        audioPlayer.volume = volumeBar.value;
        if (volumeBar.value == 0) {
            volumeButton.src = 'img/mute.png';
        } else if (volumeBar.value < 0.5) {
            volumeButton.src = 'img/volume-down.png';
        } else {
            volumeButton.src = 'img/volume-up.png';
        }
        if (isLooping) {
            loopImg.src = 'img/loop-one.png';
            loopImg.alt = 'loop on';
            loopImg.classList.add('activated');
        }
        if ($('#favorite-button')!=null){
            let url = '/TrangChu/getFavoriteSongIds?userId='+userId
            $.ajax({
                url: url,
                method: 'Get',
                success: function (data) {
                    if (data.includes(Number(songId))) {
                        favoriteSongBtn.src = 'img/favorited.png'; // Đổi hình ảnh sang nút yêu thích
                    }else{
                        favoriteSongBtn.src = 'img/notFavorited.png';
                    }
                }
            });
        }
        // Cập nhật giao diện
        updateSongInfo(songId,savedSongPicUrl, savedSongName, savedArtistName, savedGenre, savedAlbum, savedReleaseDate)

        // Phát nhạc hoặc để ở trạng thái tạm dừng
    }
};

////////////////////////////////////////////////music player section functions//////////////////////////////////////////////////
audioPlayer.addEventListener("timeupdate", () => {
    const volumeBar = document.getElementById('volume-bar');
    const songInfo = $('#song-info'); // Chọn phần tử #song-info
    localStorage.setItem("songId", songInfo.find('h6').data('songid'))
    localStorage.setItem("currentSongUrl", audioPlayer.src); // Lưu URL bài hát
    localStorage.setItem("currentTime", audioPlayer.currentTime); // Lưu thời gian phát hiện tại
    localStorage.setItem("currentSongName",songInfo.find('h6').text()); // Lưu tên bài hát
    localStorage.setItem("artistName",songInfo.find('p').text()); // Lưu tên ca sĩ
    localStorage.setItem("songPicUrl", songInfo.find('img').attr('src')); // Lưu URL hình ảnh bài hát
    localStorage.setItem("genre",songInfo.find('h6').data('genre'))
    localStorage.setItem("album",songInfo.find('h6').data('album'))
    localStorage.setItem("releaseDate",songInfo.find('h6').data('date'))
    localStorage.setItem("volume", audioPlayer.volume); // Lưu âm lượng
    if (audioPlayer.ended) {
        const isLooping = localStorage.getItem('loop')=== 'true'
        console.log(isLooping);
        if (isLooping) {
            // Nếu loop đang bật, phát lại bài hát hiện tại
            audioPlayer.currentTime = 0;
            audioPlayer.play();
        } else {
            playPause.src = 'img/replay.png';
            playPause.alt = 'replay';
        }


    }
});
playPause.addEventListener('click', () => {
    if (audioPlayer.paused) {
        audioPlayer.play(); // Phát nhạc
        playPause.src = 'img/pause-button.png'; // Đổi hình ảnh sang nút tạm dừng
        playPause.alt = 'pause';
    }
    else if (audioPlayer.ended){
        audioPlayer.currentTime = 0;
        audioPlayer.play(); // Phát lại bài hát
        playPause.src = 'img/pause-button.png'; // Đổi hình ảnh sang nút tạm dừng
        playPause.alt = 'pause';
    }
    else {
        audioPlayer.pause(); // Tạm dừng nhạc
        playPause.src = 'img/play-button.png'; // Đổi hình ảnh sang nút phát
        playPause.alt = 'play';
    }
});


// Cập nhật tổng thời gian khi bài hát tải xong
audioPlayer.addEventListener('loadedmetadata', function () {
    totalTimeDisplay.textContent = formatTime(audioPlayer.duration);
    progressBar.max = Math.floor(audioPlayer.duration);
});

// Cập nhật thời gian hiện tại khi bài hát phát
audioPlayer.addEventListener('timeupdate', function () {
    currentTimeDisplay.textContent = formatTime(audioPlayer.currentTime);
    progressBar.value = Math.floor(audioPlayer.currentTime);
});

// Người dùng chỉnh thời gian
progressBar.addEventListener('input', function () {
    audioPlayer.currentTime = progressBar.value;
});

// Hàm định dạng thời gian
function formatTime(seconds) {
    const minutes = Math.floor(seconds / 60);
    const secs = Math.floor(seconds % 60);
    return `${minutes}:${secs < 10 ? '0' : ''}${secs}`;
}
// Cập nhật âm lượng khi người dùng chỉnh thanh âm lượng
volumeBar.addEventListener('input', function () {
    audioPlayer.volume = volumeBar.value;
    if (volumeBar.value == 0) {
        volumeButton.src = 'img/mute.png';
    } else if (volumeBar.value < 0.5) {
        volumeButton.src = 'img/volume-down.png';
    } else {
        volumeButton.src = 'img/volume-up.png';
    }
});
// Hàm chuyển âm lượng khi người dùng nhấn nút mở/tắt âm lượng

volumeButton.addEventListener('click', function () {
    if (audioPlayer.volume > 0.5) {
        // Giảm âm lượng xuống 50%
        audioPlayer.volume = 0.5;
        volumeBar.value = 0.5;
        volumeButton.src = 'img/volume-down.png';
    } else if (audioPlayer.volume > 0) {
        // Tắt âm lượng
        audioPlayer.volume = 0;
        volumeBar.value = 0;
        volumeButton.src = 'img/mute.png';
    } else {
        // Bật lại âm lượng tối đa
        audioPlayer.volume = 1;
        volumeBar.value = 1;
        volumeButton.src = 'img/volume-up.png';
    }
});
////////////////////////////////confirmaction//////////////////////////////////////
function confirmAction() {
    const confirmBtn = document.getElementById('confirmBtn');
    const modalTitle = document.getElementById('actionModalLabel');
    const modalBody = document.getElementById('modalBodyContent');

        confirmBtn.href = `/logout`;
        modalTitle.innerText = 'Xác nhận đăng xuất';
        modalBody.innerText = 'Bạn có chắc chắn muốn đăng xuất?';

    const modal = new bootstrap.Modal(document.getElementById('actionModal'));
    modal.show();
}