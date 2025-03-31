
/////////////////////////////// artist functions//////////////////////////////////////////////////////////////////////////////
function AllArtists() {
    if (artistsList.classList.contains('d-none')) {
        artistsList.classList.remove('d-none')
    }
    songsList.classList.add('d-none');
    playLists.classList.add('d-none');
    genresList.classList.add('d-none');
    document.getElementById('artist-songs-title').classList.add('d-none');
    $('#playlist-songs').html('')
    $('#artist-songs-tbody').html('')
    $('#genre-songs-tbody').html('')
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
                    <div class="card transparent-card no-border shadow-sm mb-5" style="width: 200px; height: 250px; ">
                    <div style="">
                            <img src="${artist.artistPicUrl}" alt="Ảnh bìa" class="card-img-top" style="position: relative; width: 100%; height: 250px; object-fit: cover;">
                            <div style="position: absolute; bottom: 0; left: 0; right: 0; padding: 10px;" >
                                    <img src="img/play-playlist.png" id="instantPlay-button" onclick="instantPlayButtonArtist('${artist.artistName}')" class="shadow-sm circle-button" style="cursor: pointer;" width="50px" height="50px" alt="play">
                                </div>
                            <div class="card-body text-center" id="card-title">
                                <h6 class="card-title text-white" style="cursor: pointer;" onclick="getArtistSongs('${artist.artistName}')">${artist.artistName}</h6>
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
    songsList.classList.add('d-none');
    playLists.classList.add('d-none');
    artistsList.classList.add('d-none');
    genresList.classList.add('d-none');
    document.getElementById('artist-songs-title').classList.remove('d-none');
    const url2 = '/TrangChu/getArtistSongs?artist='+ artistName;
    // get playlist songs
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
                $('#artist-songs-tbody').append(songHtml);
            });
            setupPlaylistButtons(playlist);

        },
        error: function (xhr, status, error) {
            console.error('Lỗi khi tải bài hát:', xhr.responseText || status, error);
            $('#artist-songs-tbody').html('<p>Không thể tải danh sách bài hát. Vui lòng thử lại sau.</p>');
        }
    });

}