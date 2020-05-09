var contextPath = "http://localhost:8080";

$(document).on("click",".edit-Artesano", function(){
    $("#editModal  #codigo").val(' ');$("#editModal  #nombre").val('');
    $("#editModal  #apellidopaterno").val('');$("#editModal  #apellidomaterno").val('');
    $.ajax({
        method:"GET", url:contextPath + "/gestor/artesano/get?id=" + $(this).data('id')
    }).done(function(artesano){
        if (artesano!=null){
            $("#editModal  #codigo").val(artesano.codigo).prop("readonly", true);
            $("#editModal #nombre").val(artesano.nombre);
            $("#editModal  #apellidopaterno").val(artesano.apellidopaterno);
            $("#editModal  #apellidomaterno").val(artesano.apellidomaterno);
            $("#editModal  #comunidades").val(artesano.comunidades.codigo)

        }
    }).fail(function (err) {
        console.log(err);
        $('#editModal').modal('hide');
        alert("Ocurrió un error");
    })
});
$(document).on("click",".new-Artesano", function(){
    $("#editModal  #codigo").val('');$("#editModal  #nombre").val('');
    $("#editModal  #apellidopaterno").val('');$("#editModal  #apellidomaterno").val('');
});
$(document).on("click",".delete-Artesano", function(){
    $("#deleteModal #codigo").val($(this).data('id'));
});
$(document).ready(function() {
    if ($("#msgArtesanos").text()==="ERROR"){
        $('#editModal').modal('show');
    }
});

