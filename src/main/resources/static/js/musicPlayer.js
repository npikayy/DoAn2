
window.onload = function () {
    getEverything()
};
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
    $('#playlist-songs').html('');
    $('#artist-songs-tbody').html('')
    $('#genre-songs-tbody').html('')
}
function getEverything() {
    getAllSongs();
    getAllPlaylists();
    getAllArtists();
    getAllGenres()
    const savedSongUrl = localStorage.getItem("currentSongUrl");
    const savedTime = localStorage.getItem("currentTime");
    const savedSongName = localStorage.getItem("currentSongName");
    const savedArtistName = localStorage.getItem("artistName");
    const savedSongPicUrl = localStorage.getItem("songPicUrl");
    const savedVolume = localStorage.getItem("volume");
    const isLooping = localStorage.getItem('loop') === 'true';
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
        // Cập nhật giao diện
        document.getElementById("song-info").innerHTML = `
            <img src="${savedSongPicUrl}" alt="${savedSongName}" class="rounded-circle me-2" width="60" height="60">
            <div style="height: 100px; padding-top: 10px;" >
                <h7 style="color: #ffffff; height: 100px;">${savedSongName}</h7>
                <p style="color: #e7e7e7;">${savedArtistName}</p>
            </div>
            <img src="img/notFavorited.png" alt="comment" class="mb-2" width="35px" height="35px">
        `;

        // Phát nhạc hoặc để ở trạng thái tạm dừng
    }
};

////////////////////////////////////////////////music player section functions//////////////////////////////////////////////////
audioPlayer.addEventListener("timeupdate", () => {
    const volumeBar = document.getElementById('volume-bar');
    const songInfo = $('#song-info'); // Chọn phần tử #song-info
    localStorage.setItem("currentSongUrl", audioPlayer.src); // Lưu URL bài hát
    localStorage.setItem("currentTime", audioPlayer.currentTime); // Lưu thời gian phát hiện tại
    localStorage.setItem("currentSongName",songInfo.find('h7').text()); // Lưu tên bài hát
    localStorage.setItem("artistName",songInfo.find('p').text()); // Lưu tên ca sĩ
    localStorage.setItem("songPicUrl", songInfo.find('img').attr('src')); // Lưu URL hình ảnh bài hát
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