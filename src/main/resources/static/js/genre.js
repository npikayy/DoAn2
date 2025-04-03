
/////////////////////////////// artist functions//////////////////////////////////////////////////////////////////////////////
function AllGenres() {
    if (genresList.classList.contains('d-none')) {
        genresList.classList.remove('d-none')
    }
    songsList.classList.add('d-none');
    playLists.classList.add('d-none');
    artistsList.classList.add('d-none');
    document.getElementById('artist-songs').classList.add('d-none');
    document.getElementById('playlist-songs').classList.add('d-none');
    $('#playlist-songs').html('')
    $('#artist-songs').html('')
    $('#genre-songs').html('')

}
function getRandomRGBColor() {
    const r = Math.floor(Math.random() * 256);
    const g = Math.floor(Math.random() * 256);
    const b = Math.floor(Math.random() * 256);
    return `rgb(${r}, ${g}, ${b})`;
}
function getAllGenres() {
    $.ajax({
        url: '/TrangChu/getAllGenres', // API tải danh sách bài hát
        method: 'GET',
        dataType: 'json', // Đảm bảo nhận dữ liệu JSON
        success: function (data) {
            let genresHtml = `<h4 class="w-100 text-white mb-3">Danh sách Thể loại</h4>`;

            // Hàm tạo màu nền ngẫu nhiên
            function getRandomColor() {
                const letters = '0123456789ABCDEF';
                let color = '#';
                for (let i = 0; i < 6; i++) {
                    color += letters[Math.floor(Math.random() * 16)];
                }
                return color;
            }

            // Xử lý dữ liệu trả về từ API
            data.forEach(function (genre) {
                const randomBackground = getRandomColor(); // Lấy màu nền ngẫu nhiên
                genresHtml += `
                    <div class="card no-border shadow-sm" style="width: 200px; background-color: ${randomBackground};">
                            <div class="card-body text-center " id="card-title">
                                <h6 class="card-title text-white m-auto" style="cursor: pointer;" onclick="getGenreSongs('${genre.genreName}')">${genre.genreName}</h6>
                            </div>
                    </div>
                `;
            });

            // Hiển thị danh sách thể loại
            $('#genres-list').html(genresHtml);
        },
        error: function (xhr, status, error) {
            console.error('Lỗi khi tải danh sách thể loại:', xhr.responseText || status, error);
            $('#genres-list').html('<p>Không thể tải danh sách thể loại. Vui lòng thử lại sau.</p>');
        }
    });
}
function getGenreSongs(genreName) {
    scrollToTop();
    songsList.classList.add('d-none');
    playLists.classList.add('d-none');
    artistsList.classList.add('d-none');
    document.getElementById('artist-songs').classList.add('d-none');
    document.getElementById('playlist-songs').classList.add('d-none');
    genresList.classList.add('d-none');
    // get playlist songs
    const url2 = '/TrangChu/getGenreSongs?genre='+ genreName;
    $.ajax({
        url: url2, // API tải danh sách bài hát
        method: 'GET',
        dataType: 'json', // Đảm bảo nhận dữ liệu JSON
        success: function (playlist) {
            let index = -1;
            let songsHtml =
                `<h4 class="w-100 text-white mb-3">Danh sách bài hát</h4>`;
            playlist.forEach(function (song) {
                index++;
                songsHtml += `
                <div class="row m-1">
                        <div class="col" style="width: 230px">
                            <div class="card transparent-card no-border shadow-sm">
                                <img src="${song.songPicUrl}" class="card-img-top" alt="Ảnh bìa">
                                <div class="card-body text-center" id="card-title">
                                <h6 class="card-title text-white playlist-song" style=" cursor: pointer;" data-index="${index}" data-songid="${song.songId}" data-date="${song.releaseDate}" data-genre="${song.genre}" data-album="${song.album}" data-songpicurl="${song.songPicUrl}" data-artist="${song.artist}" data-url="${song.songUrl}" data-songname="${song.songName}">${song.songName}</h6>
                                </div>
                            </div>
                        </div>
                    </div>
                `
                $('#genre-songs').html(songsHtml);
                // document.getElementById('artist-songs-tbody').innerHTML = `songHtml`;
            });

            setupPlaylistButtons(playlist);
        },
        error: function (xhr, status, error) {
            console.error('Lỗi khi tải bài hát:', xhr.responseText || status, error);
            $('#genre-songs-tbody').html('<p>Không thể tải danh sách bài hát. Vui lòng thử lại sau.</p>');
        }
    });

}