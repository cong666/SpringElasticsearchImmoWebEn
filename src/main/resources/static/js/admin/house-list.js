var table = $('#data-table').DataTable({
    "ordering": true,
    "order": [[7, "desc"]],//Order by createtime by efault
    "paging": true, // if close paging
    "lengthMenu": [[5, 25, 50, -1], [5, 25, 50, "All"]],
    "info": true, // display search info
    "autoWidth": true, // Datatable with : auto
    "stateSave": false, // allow the cache forDatatables
    "retrieve": true, // If it has already been initialized, continue to use the previous Datatables instance
    "processing": true, // Show the status being processed
    "serverSide": true, // Server mode, data is controlled by the server
    "pagingType": "simple_numbers", // Page flip display: Previous and next two buttons, plus page button
    "language": {
        "sProcessing": "Processing...",
         "lengthMenu": "Display _MENU_ records per page",
         "zeroRecords": "Nothing found - sorry",
         "info": "Showing page _PAGE_ of _PAGES_",
         "infoEmpty": "No records available",
         "infoFiltered": "(filtered from _MAX_ total records)"

    },
    columns: [{ // Binding the data
        data: "id",
    }, {
        data: "title",
        searchable: false //
    }, {
        data: "cover",
        searchable: false,
        orderable: false
    }, {
        data: "area",
    }, {
        data: "price",
        orderable: true
    }, {
        data: "floor",
    }, {
        data: "watchTimes",
    }, {
        data: "createTime"
    }, {
        data: "status",
        orderable: false
    }, {
        data: null
    }],
    columnDefs: [{ // Datable Style
        targets: 0,
        render: function (data, type, row, meta) {
            return '<td class="text-l"><u style="cursor:pointer" class="text-primary"' +
                'onClick="house_edit(\'View\', \'/admin/house/show?id=' + data + '\')" title="View">' + data + '</u></td>';
        }
    }, {
        targets: 1,
        render: function (data, type, row, meta) {
            return '<td class="text-l"><u style="cursor:pointer" class="text-primary"' +
                'onClick="house_edit(\'View\', \'/admin/house/show?id=' + row.id + '\')" title="View">' + data + '</u></td>';
        }
    }, {
        targets: 2,
        render: function (data, type, row, meta) {
            return '<td><img onClick="house_edit(\'View\', \'/admin/house/show?id=' + row.id + '\')" title="View"' +
                ' class="picture-thumb" src="http://localhost:8080/' + data + '?imageView2/1/w/200/h/100"></td>';
        }
    }, {
        targets: 3,
        orderData:[3]
    },{
        targets: 7,
        render: function (data, type, row, meta) {
            return (new Date(data)).Format("yyyy-MM-dd hh:mm:ss");
        }
    }, {
        targets: 8,
        render: function (data, type, row, meta) {
            var html = '';
            if (data === 0) {
                html = '<td class="td-status"><span class="label label-danger radius">To Verify</span></td>';
            } else if (data === 1) {
                html = '<td class="td-status"><span class="label label-success radius">Published</span></td>';
            } else if (data === 2) {
                html = '<td class="td-status"><span class="label label-warning radius">Rented</span></td>';
            } else {
                html = '<td class="td-status"><span class="label label-danger radius">Unknown State</span></td>';
            }
            return html;
        }
    }, {
        targets: 9,
        render: function (data, type, row, meta) {
            var prefix = '<td class="f-14 td-manage">',
                data_status = row.status,
                content = '',
                suffix = '<a style="text-decoration:none" class="ml-5"' +
                    ' onClick="house_edit(\'Edit\', \'/admin/house/edit?id=' + row.id + '\')" href="javascript:;"' +
                    ' title="编辑"><i class="Hui-iconfont">&#xe6df;</i></a> <a style="text-decoration:none" class="ml-5"' +
                    ' onClick="house_del(this, ' + row.id + ')" href="javascript:;" title="Delete"><i' +
                    ' class="Hui-iconfont">&#xe6e2;</i></a></td>';
            if (data_status === 0) { // To Verify
                content = '<a style="text-decoration:none" onClick="house_pass(this,' + row.id + ')"' +
                    ' href="javascript:;" title="Publish">Publish</a>&nbsp;';
            } else if (data_status === 1) { // Published
                content = '<a style="text-decoration:none" onClick="house_stop(this,' + row.id + ')"' +
                    ' href="javascript:;" title="Not Available"><i class="Hui-iconfont">&#xe6de;</i></a>&nbsp;'
            }

            return prefix + content + suffix;
        }
    }],
    ajax: {
        type: "POST",
        url: "/admin/houses",
        cache: false,
        data: function (data) {
            var postData = {},
             houseStatus = $('#houseStatus').val(),
                createTimeMin = $('#createTimeMin').val(),
                createTimeMax = $('#createTimeMax').val(),
                city = $('#city').val(),
                title = $('#houseTitle').val();
            if (houseStatus.length > 0) {
                postData.status = houseStatus;
            }
            if (city.length > 0) {
                postData.city = city;
            }
            if (title.length > 0) {
                postData.title = title;
            }

            var orderColumn = data.columns[data.order[0].column];
            postData.direction = data.order[0].dir;
            postData.orderBy = orderColumn.data;

            postData.createTimeMin = createTimeMin;
            postData.createTimeMax = createTimeMax;
            postData.draw = data.draw;
            postData.start = data.start;
            postData.length = data.length;
            return postData;
        }

    }
});

$(function () {
    var $city = $("#city");

    // binding the city
    $.get('/address/support/cities', function (data, status) {
        if (status !== 'success' || data.code !== 200) {
            alert("Error: " + data.message);
            return;
        }
        var str = '';
        $.each(data.data, function (i, item) {
            str += "<option value=" + item.en_name + ">" + item.cn_name + "</option>";
        });
        $city.append(str);
    });

});

$('#houseSearch').on('click', null, function () {
    reloadTable();
});

function reloadTable() {
    table.ajax.reload(null, false);
}

function house_edit(title, url) {
    var index = layer.open({
        type: 2,
        title: title,
        content: url
    });
    layer.full(index);
}

function house_del(obj, id) {
    layer.confirm('Are you sure？', function (index) {
        $.ajax({
            type: 'PUT',
            url: '/admin/house/operate/' + id + '/' + '3',
            success: function (data) {
                if (data.code === 200) {
                    $(obj).parents("tr").remove();
                    layer.msg('Deleted!', {icon: 1, time: 1000});
                    reloadTable();
                } else {
                    layer.msg('Failed to delete！' + data.message, {icon: 5, time: 1000});

                }

            },
            error: function (jqXHR, textStatus, errorThrown) {
                console.log(jqXHR);
                layer.msg('Failed to delete！' + jqXHR.responseText, {icon: 5, time: 3000});
            }
        });
    });
}

/*房源-下架*/
function house_stop(obj, id) {
    layer.confirm('Are you sure？', function (index) {
        $.ajax({
            type: 'PUT',
            url: '/admin/house/operate/' + id + '/' + '2',
            success: function (data) {
                if (data.code === 200) {
                    $(obj).parents("tr").find(".td-manage").prepend('<a style="text-decoration:none"' +
                        ' onClick="house_pass(this,id)" href="javascript:;" title="发布"><i' +
                        ' class="Hui-iconfont">&#xe603;</i></a>');
                    $(obj).parents("tr").find(".td-status").html('<span class="label label-defaunt radius">已下架</span>');
                    $(obj).remove();
                    layer.msg('You have invalidate it successful', {icon: 5, time: 1000});
                    reloadTable();
                } else {
                    layer.msg('Failed to invalidate it ！' + data.message, {icon: 5, time: 1000});
                }

            },
            error: function (jqXHR, textStatus, errorThrown) {
                console.log(jqXHR);
                layer.msg('Failed to invalidate it！' + jqXHR.responseText, {icon: 5, time: 3000});
            }
        });
    });
}

function house_pass(obj, id) {
    layer.confirm('Are you sure？', function (index) {
        $.ajax({
            type: 'PUT',
            url: '/admin/house/operate/' + id + '/' + '1',
            success: function (data) {
                if (data.code === 200) {
                    $(obj).parents("tr").find(".td-manage").prepend('<a style="text-decoration:none"' +
                        ' onClick="house_stop(this,id)" href="javascript:;" title="Invalidate"><i' +
                        ' class="Hui-iconfont">&#xe6de;</i></a>');
                    $(obj).parents("tr").find(".td-status").html('<span class="label label-success radius">Published</span>');
                    $(obj).remove();
                    layer.msg('Published successful!', {icon: 6, time: 1000});
                    reloadTable();
                } else {
                    layer.msg('Failed to publish！' + data.message, {icon: 5, time: 1000});
                }
            },
            error: function (jqXHR, textStatus, errorThrown) {
                console.log(jqXHR);
                layer.msg('Failed to publish！' + jqXHR.responseText, {icon: 5, time: 3000});
            }
        });


    });
}
