
/////////////////////////////// artist functions//////////////////////////////////////////////////////////////////////////////
function AllArtists() {
    if (artistsList.classList.contains('d-none')) {
        artistsList.classList.remove('d-none')
    }
    songsList.classList.add('d-none');
    playLists.classList.add('d-none');
    genresList.classList.add('d-none');
    document.getElementById('playlist-songs').classList.add('d-none');
    $('#playlist-songs').html('')
    $('#artist-songs').html('')
    $('#genre-songs').html('')

}
function getAllArtists() {
    $.ajax({
        url: '/TrangChu/getAllArtists', // API tải danh sách bài hát
        method: 'GET',
        dataType: 'json', // Đảm bảo nhận dữ liệu JSON
        success: function (data) {
            let artistsHtml = `<h4 class="w-100 text-white mb-3">Danh sách nghệ sĩ</h4>`;
            // console.log(data); // Ghi log dữ liệu để kiểm tra
            data.forEach(function (artist) {
                artistsHtml += `
                    <div class="card transparent-card no-border shadow-sm mb-5" style="width: 280px; height: 250px; ">
                    <div style="">
                            <img src="${artist.artistPicUrl}" alt="Ảnh bìa" class="card-img-top" style="position: relative; width: 100%; height: 250px; object-fit: cover;">
                            <div style="position: absolute; bottom: 0; left: 0; right: 0; padding: 10px;" >
                                    <img src="img/play-playlist.png" id="instantPlay-button" onclick="instantPlayButtonArtist('${artist.artistName}')" class="shadow-sm circle-button" style="cursor: pointer;" width="50px" height="50px" alt="play">
                                </div>
                            <div class="card-body text-center" id="card-title">
                                <h5 class="card-title text-white" style="cursor: pointer;" onclick="getArtistSongs('${artist.artistName}')">${artist.artistName}</h5>
                            </div>
                    </div
                        <table class="table table-bordered transparent-card">
                            <tbody></tbody>
                        </table>
                    </div>
                `;
            });
            // Nếu không có bài hát, hiển thị thông báo
            if (!artistsHtml) {
                artistsHtml = '<p>Không có bài hát nào trong danh sách.</p>';
            }
            $('#artists-list').html(artistsHtml); // Hiển thị danh sách bài hát
        },
        error: function (xhr, status, error) {
            console.error('Lỗi khi tải bài hát:', xhr.responseText || status, error);
            $('#artists-list').html('<p>Không thể tải danh sách ca sĩ. Vui lòng thử lại sau.</p>');
        }
    });
}
function instantPlayButtonArtist(artistName) {
    const url = '/TrangChu/getArtistSongs?artist='+ artistName;
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
function getArtistSongs(artistName) {
    scrollToTop();
    songsList.classList.add('d-none');
    playLists.classList.add('d-none');
    artistsList.classList.add('d-none');
    genresList.classList.add('d-none');
    document.getElementById('artist-songs').classList.remove('d-none');
    document.getElementById('playlist-songs').classList.add('d-none');
    const url2 = '/TrangChu/getArtistSongs?artist='+ artistName;
    // get playlist songs
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
                $('#artist-songs').html(songsHtml);
            });
            setupPlaylistButtons(playlist);

        },
        error: function (xhr, status, error) {
            console.error('Lỗi khi tải bài hát:', xhr.responseText || status, error);
            $('#artist-songs-tbody').html('<p>Không thể tải danh sách bài hát. Vui lòng thử lại sau.</p>');
        }
    });

}