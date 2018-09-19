/**
 * Created by Ccong.
 */

Date.prototype.Format = function(fmt) {

    var o = {
        "M+": this.getMonth() + 1,
        //Month

        "d+": this.getDate(),
        //day

        "h+": this.getHours(),
        //Hour

        "m+": this.getMinutes(),
        //Minute

        "s+": this.getSeconds(),
        //Second

        "q+": Math.floor((this.getMonth() + 3) / 3),
        //季度quarter

        "S": this.getMilliseconds() //Millisecond

    };
    if (/(y+)/.test(fmt)) {
        fmt = fmt.replace(RegExp.$1, (this.getFullYear() + "").substr(4 - RegExp.$1.length));
    }
    for (var k in o) {
        if (new RegExp("(" + k + ")").test(fmt)) {
            fmt = fmt.replace(RegExp.$1, (RegExp.$1.length == 1) ? (o[k]) : (("00" + o[k]).substr(("" + o[k]).length)));
        }
    }
    return fmt;
};

function parent_reload() {
    window.parent.location.reload();
}

function mySelfInfo(userId) {
    $.get('/admin/user/' + userId, function (res) {
        layer.open({
            type: 1,
            title: false,
            closeBtn: false,
            area: '300px;',
            shade: 0.8,
            id: 'LAY_layuipro',
            btn: ['Close'],
            moveType: 1,
            content: '<div style="padding: 50px; line-height: 22px; background-color: #393D49; color: #fff;' +
            ' font-weight: 300;">User ：' + res.data.name + '<br>Phone ：' + res.data.phoneNumber + '</div>'
        });
    });
}