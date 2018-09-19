//Import , control the pageable
var table = $('#data-table').DataTable({
        "pageLength": 3,
        "paging": true,
        "lengthChange": false,
        "searching": false,
        "ordering": true, //
        "info": true, //
        "autoWidth": true, //
        "stateSave": false, //
        "retrieve": true, //
        "processing": true, //
        "serverSide": true, //
        "pagingType": "simple_numbers",
        "language": {
                "sProcessing": "Processing...",
                "sLengthMenu": "Display _MENU_ Results",
                "sZeroRecords": "No Records Found",
                "sInfo": "Display From _START_ To _END_ Results，Have _TOTAL_ Items",
                "sInfoEmpty": "Display from 0 To 0 Results，Have 0 Items",
                "sInfoFiltered": "(By _MAX_ Filter)",
                "sInfoPostFix": "",
                "sUrl": "",
                "sEmptyTable": "No Records Found",
                "sLoadingRecords": "Loading...",
                "sInfoThousands": ",",
                "oPaginate": {
                    "sFirst": "First Page",
                    "sPrevious": "Previous Page",
                    "sNext": "Next Page",
                    "sLast": "Last Page"
                },
                "oAria": {
                    "sSortAscending": ": Ascending",
                    "sSortDescending": ": Descending"
                }
            },
        columns: [{
            data: "first.id",
        }, {
            data: "first.title",
            searchable: false
        }, {
            data: "first.cover",
            searchable: false,
            orderable: false
        }, {
            data: "first.area"
        }, {
            data: "first.price"
        }, {
            data: "first.floor"
        }, {
            data: "first.watchTimes"
        }, {
            data: "second.orderTime"
        }, {
            data: null
        }],
        columnDefs: [{ //
            targets: 0,
            render: function (data, type, row, meta) {
                return '<td class="text-l"><u style="cursor:pointer" class="text-primary"' +
                    'onClick="house_edit(\'View\', \'/admin/house/show?id=' + data + '\')" title="View">' + data + '</u></td>';
            }
        }, {
            targets: 1,
            render: function (data, type, row, meta) {
                return '<td class="text-l"><u style="cursor:pointer" class="text-primary"' +
                    'onClick="house_edit(\'View\', \'/house/show?id=' + row.first.id + '\')" title="View">' + data + '</u></td>';
            }
        }, {
            targets: 2,
            render: function (data, type, row, meta) {
                return '<td><img onClick="house_edit(\'View\', \'/house/show?id=' + row.first.id + '\')" title="View"' +
                    ' class="picture-thumb" src="http://7xo6gy.com1.z0.glb.clouddn.com/' + data + '?imageView2/1/w/200/h/100"></td>';
            }
        }, {
            targets: 7,
            render: function (data, type, row, meta) {
                return (new Date(data)).Format("yyyy-MM-dd");
            }
        }, {
            targets: 8,
            render: function (data, type, row, meta) {
                return '<a style="text-decoration: underline" onclick="userTip(' + row.second.userId + ')">View User</a>'
            }
        }, {
            targets: 9,
            render: function (data, type, row, meta) {
                return '<td class="f-14 td-manage"><a style="text-decoration:none" class="ml-5"' +
                    ' onClick="finishSubscribe(this,' + row.first.id + ')" href="javascript:;"' +
                    ' title="Finishit"><i class="Hui-iconfont">&#xe603;</i></a></td>';
            }
        }],
        ajax: {
            type: "GET",
            url: "/admin/house/subscribe/list",
            cache: false,
            data: function (data) {
                var params = {};
                params.draw = data.draw;
                params.start = data.start;
                params.length = data.length;
                return params;
            }

        }
    });

function reloadTable() {
    table.ajax.reload(null, false);
}

function userTip(userId) {
    $.get('/admin/user/' + userId, function (res) {
        layer.open({
            type: 1,
            title: false,
            closeBtn: false,
            area: '300px;',
            shade: 0.8,
            id: 'LAY_layuipro',
            btn: ['Close'],
            moveType: 1, //drag and drop mode
            content: '<div style="padding: 50px; line-height: 22px; background-color: #393D49; color: #fff;' +
            ' font-weight: 300;">User：' + res.data.name + '<br>Phone：' + res.data.phoneNumber + '</div>'
        });
    });
}

function finishSubscribe(obj, id) {
    layer.confirm('Have you already guide your client？', function () {
        $.ajax({
            type: 'POST',
            url: '/admin/finish/subscribe',
            data: {
                house_id: id
            },
            success: function (data) {
                if (data.code === 200) {
                    $(obj).parents("tr").remove();
                    layer.msg('Finished!', {icon: 1, time: 1000});
                    reloadTable();
                } else {
                    layer.msg('Failed！' + data.message, {icon: 5, time: 1000});

                }

            },
            error: function (jqXHR, textStatus, errorThrown) {
                layer.msg('Failed！' + jqXHR.responseText, {icon: 5, time: 3000});
            }
        });
    });
}