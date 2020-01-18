/**********************************************************************************************************
 *                                       ADMIN AREA
 **********************************************************************************************************
 **********************************************************************************************************/
console.log("Rest admin loaded");
var frm = $('#rest-configuration');
frm.submit(function (e) {
    e.preventDefault();
    $.ajax({
        type: frm.attr('method'),
        url: frm.attr('action'),
        data: frm.serialize(),
        success: function (data) {
            console.log('Submission was successful.');
            console.log(data);
        },
        error: function (data) {
            console.log('An error occurred.');
            console.log(data);
        },
    });
});

