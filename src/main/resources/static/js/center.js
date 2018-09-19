/**
 * Created by Ccong.
 */

$(function () {
    $('.xunwu-header .center-page').addClass('on');

    $('.user-left .select a').on('click', function () {
        $('.user-left .select li').each(function () {
            $(this).removeClass('hover');
        });
        $(this).parent().addClass('hover');

        var bind = "." + $(this).attr('data-bind');
        $('.right-body').each(function () {
            $(this).removeClass('on');
        });
        $(bind).addClass('on');
        selectDataSource(bind);
    });

    $('.user-right .right-body .tab span').on('click', function () {
        $(this).parent().find('span').each(function () {
            $(this).removeClass('hover');
        });
        $(this).addClass('hover');

        var bind = "." + $(this).attr('data-bind');
        $('.user-right .modify-tab .modify-content').each(function () {
            $(this).removeClass('on');
        });
        $(bind).addClass('on');
    });


    $('.user-right .wait-record button').on('click', function () {
        var selected = $('.user-right .wait-record input[name="houseId"]:checked').val();
        if (typeof (selected) === 'undefined') {
            layer.msg('请选择要预约的房源', {icon: 5, time: 2000});
            return false;
        }

        $.ajax({
            url: '/api/user/house/subscribe/date',
            type: 'POST',
            data: $('.user-right .wait-record form').serialize(),
            success: function (data) {
                if (data.code === 200) {
                    $('.user-right .wait-record input[name="houseId"]:checked').parent().remove();
                    layer.msg('Success', {icon: 6, time: 2000});

                } else {
                    layer.msg(data.message, {icon: 5, time: 2000})
                }
            },
            error: function (xhr, response, error) {
                layer.msg(response, {icon: 5, time: 3000});
            }
        });
        return false;
    });

    $('.user-right .subscribe button').on('click', function () {
        var selected = $('.user-right .subscribe input[name="houseId"]:checked').val();
        if (typeof (selected) === 'undefined') {
            layer.msg('Please choose one item', {icon: 5, time: 2000});
            return false;
        }

        layer.confirm('Are you sure?', {
            btn: ['Yes', 'No']
        }, function () {
            $.ajax({
                url: '/api/user/house/subscribe?houseId=' + selected,
                type: 'DELETE',
                success: function (data) {
                    if (data.code === 200) {
                        $('.user-right .subscribe input[name="houseId"]:checked').parent().parent().remove();
                        layer.msg('Canceled!', {icon: 6, time: 2000});
                    } else if (data.code === 403) {
                        layer.msg('Please login firstly', {icon: 5, time: 2000});
                    } else {
                        layer.msg(data.message, {icon: 5, time: 2000});
                    }
                },
                error: function (xhr, response, error) {
                    if (xhr.status === 403) {
                        layer.msg('Please login firstly', {icon: 5, time: 2000});
                    } else {
                        layer.msg('Server Error: ' + response, {icon: 5, time: 3000});
                    }
                }
            })
        });
    });

    loadWaitRecord();

    $('.edit-info .nameSubmit').on('click', function () {
        var name = $('.modify-tab input[name="name"]').val();
        if (typeof (name) === 'undefined' || name === null || name.length < 1) {
            layer.msg('Please input the name', {icon: 5, time: 2000});
            return false;
        }

        $.ajax({
            url: '/api/user/info',
            data: {
                profile: 'name',
                value: name
            },
            type: 'POST',
            success: submitSuccess,
            error: submitError
        });
    });

    $('.edit-info .emailSubmit').on('click', function () {
        var email = $('.modify-tab input[name="email"]').val();
        if (typeof (email) === 'undefined' || email === null || email.length < 1) {
            layer.msg('Please input the email', {icon: 5, time: 2000});
            return false;
        }

        var emailPattern = /^[a-zA-Z0-9_-]+@[a-zA-Z0-9_-]+(\.[a-zA-Z0-9_-]+)+$/;
        var n = email.search(emailPattern);
        if (n < 0) {
            layer.msg('Please input the correct email', {icon: 5, time: 2000});
            return false;
        }


        $.ajax({
            url: '/api/user/info',
            data: {
                profile: 'email',
                value: email
            },
            type: 'POST',
            success: submitSuccess,
            error: submitError
        });
    });

    $('.edit-info .passwordSubmit').on('click', function () {
        var password = $('.modify-tab input[name="password"]').val();
        if (typeof (password) === 'undefined' || password === null || password.length < 1) {
            layer.msg('Please input the password', {icon: 5, time: 2000});
            return false;
        }

        $.ajax({
            url: '/api/user/info',
            data: {
                profile: 'password',
                value: password
            },
            type: 'POST',
            success: submitSuccess,
            error: submitError
        });
    });

    function submitSuccess(data) {
        if (data.code === 200) {
            layer.msg('Success!', {icon: 6, time: 2000});
        } else if (data.code === 403) {
            layer.msg('Failed!', {icon: 5, time: 2000});
        } else {
            layer.msg(data.message, {icon: 5, time: 2000});
        }
    }
    
    function submitError(xhr, response, e) {
        if (xhr.status === 403) {
            layer.msg('Invalidate!', {icon: 5, time: 2000});
        } else {
            layer.msg('Server Error: ' + response, {icon: 5, time: 3000});
        }
    }

});

function selectDataSource(bind) {
    switch (bind) {
        case '.wait-record':
            loadWaitRecord();
            break;
        case '.subscribe':
            loadSubscribeList();
            break;
        case '.finish-record':
            loadFinishList();
            break;
        default:
            break;
    }
}

/**
 */
function loadWaitRecord() {
    layui.use('flow', function () {
        var $ = layui.jquery;
        var flow = layui.flow;
        flow.load({
            elem: '#wait-record-list',
            scrollElem: '.wait-record .content',
            done: function (page, next) {
                var lis = [],
                    start = (page - 1) * 3;

                $.get('/api/user/house/subscribe/list?status=1' + '&start=' + start + '&size=3',
                    function (res) {
                        layui.each(res.data, function (index, tuple) {
                            var house = tuple.first;
                            var content = '<li><input type="radio" name="houseId" value="' + house.id + '">Reserve the house'
                                + '<div class="cover fl">'
                                + '<img src="http://7xo6gy.com1.z0.glb.clouddn.com/' + house.cover + '" width="100px" height="80px"></div> ' +
                                '<div class="info fl"><a><h1>' + house.title + '</h1></a><div class="des1">' +
                                '<i></i><span><a href="#" target="_blank">' + house.district + '</a></span>' +
                                '<span class="line">|</span><span>' + house.room + 'Room' + house.parlour + 'Parlour</span>'
                                + '<span class="line">|</span><span>' + house.area + 'square meter</span></div><div class="des2">'
                                + '<i></i><span>' + house.floor + 'floor(Have' + house.totalFloor + 'floors)</span><span>' +
                                house.buildYear + 'Built</span><span>-</span><span>' + house.street + '</span><span>' +
                                house.price + '$/Month</span></div></div></li>';
                            lis.push(content);
                        });

                        next(lis.join(''), res.more);
                    });
            }
        })
    });
}

/**
 */
function loadSubscribeList() {
    layui.use('flow', function () {
        var $ = layui.jquery;
        var flow = layui.flow;
        flow.load({
            elem: '.subscribe .content ul',
            scrollElem: '.subscribe .content',
            done: function (page, next) {
                var lis = [],
                    start = (page - 1) * 3;

                $.get('/api/user/house/subscribe/list?status=2' + '&start=' + start + '&size=3',
                    function (res) {
                        layui.each(res.data, function (index, tuple) {
                            var house = tuple.first,
                                subscribe = tuple.second;
                            var content = '<li><span' +
                                ' class="order-time">Reserve time：' + (new Date(subscribe.orderTime)).Format("yyyy-MM-dd")
                                + '</span><div class="cover fl">'
                                + '<img src="http://7xo6gy.com1.z0.glb.clouddn.com/' + house.cover + '" width="100px" height="80px"></div> ' +
                                '<div class="info fl"><a><h1>' + house.title + '</h1></a><div class="des1">' +
                                '<i></i><span><a href="#" target="_blank">' + house.district + '</a></span>' +
                                '<span class="line">|</span><span>' + house.room + 'room' + house.parlour + 'parlour</span>'
                                + '<span class="line">|</span><span>' + house.area + 'Square meter</span></div><div class="des2">'
                                + '<i></i><span>' + house.floor + 'Floor(Have' + house.totalFloor + 'Floor)</span><span>' +
                                house.buildYear + 'Built</span><span>-</span><span>' + house.street + '</span><span>' +
                                house.price + '$/Month</span></div></div><div class="fl"><input type="radio" name="houseId" value="' + house.id + '">Cancel</div></li>';
                            lis.push(content);
                        });

                        next(lis.join(''), res.more);
                    });
            }
        })
    });
}

/**
 */
function loadFinishList() {
    layui.use('flow', function () {
        var $ = layui.jquery;
        var flow = layui.flow;
        flow.load({
            elem: '.finish-record .content ul',
            scrollElem: '.finish-record .content',
            done: function (page, next) {
                var lis = [],
                    start = (page - 1) * 3;

                $.get('/api/user/house/subscribe/list?status=3' + '&start=' + start + '&size=3',
                    function (res) {
                        layui.each(res.data, function (index, tuple) {
                            var house = tuple.first,
                                subscribe = tuple.second;
                            var content = '<li><span' +
                                ' class="order-time">Visit Time：' + (new Date(subscribe.orderTime)).Format("yyyy-MM-dd")
                                + '</span><div class="cover fl">'
                                + '<img src="http://7xo6gy.com1.z0.glb.clouddn.com/' + house.cover + '" width="100px" height="80px"></div> ' +
                                '<div class="info fl"><a><h1>' + house.title + '</h1></a><div class="des1">' +
                                '<i></i><span><a href="#" target="_blank">' + house.district + '</a></span>' +
                                '<span class="line">|</span><span>' + house.room + 'Room' + house.parlour + 'Parlour</span>'
                                + '<span class="line">|</span><span>' + house.area + 'Square meter</span></div><div class="des2">'
                                + '<i></i><span>' + house.floor + 'Floor(Have' + house.totalFloor + 'Floor)</span><span>' +
                                house.buildYear + 'Built</span><span>-</span><span>' + house.street + '</span><span>' +
                                house.price + '$/Month</span></div></div></li>';
                            lis.push(content);
                        });

                        next(lis.join(''), res.more);
                    });
            }
        })
    });
}