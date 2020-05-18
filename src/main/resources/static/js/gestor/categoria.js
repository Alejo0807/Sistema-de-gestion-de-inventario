var contextPath  = window.location.href+"/get?id=";

$(document).on("click",".editar-Categoria", function(){
    $("#formModal  #codigo").val(' ');$("#formModal  #nombre").val('');
    $.ajax({
        method:"GET", url:contextPath + $(this).data('id')
    }).done(function(cat){
        if (cat!=null){
            $("#formModal  #codigo").val(cat.codigo).prop("readonly", true);
            $("#formModal #nombre").val(cat.nombre);
        }
    }).fail(function (err) {
        console.log(err);
        $('#formModal').modal('hide');
        alert("Ocurrió un error");
    })
});
$(document).on("click",".new-Categoria", function(){
    $("#formModal  #codigo").val('').prop("readonly", false);
    $("#formModal  #nombre").val('');
});
$(document).on("click",".delete-Categoria", function(){
    $("#deleteModal #codigo").val($(this).data('id'));
});
$(document).ready(function() {
    if ($("#msgCategoria").text()==="ERROR"){
        $('#formModal').modal('show');
    }
});

