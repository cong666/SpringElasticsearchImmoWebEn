$(function () {
    var $city = $("#city"),
        $region = $("#region"),
        $subwayLine = $("#subwayLine"),
        $subwayStation = $("#subwayStation");

      $city.on('change', function () {
          changeCity($city);
     });

    var selectedVal = $city.val();
    changeRegion($region, selectedVal);

    $city.change(function () {
          var selectedVal = $(this).val();
          if (typeof(selectedVal) == 'undefined' || selectedVal == "") {
              layer.msg('Please choose city！', {icon: 5, time: 2000});
              return;
          }

        changeRegion($region, selectedVal);
        changeSubwayLine($subwayLine, selectedVal);
    });

      $subwayLine.on('click', function () {
          var city = $city.val();
          if (typeof(city) === 'undefined' || city === "") {
             layer.msg('Please choose city！', {icon: 5, time: 2000});
              return;
          }
         changeSubwayLine($subwayLine, $city.val());
      });

    $subwayLine.change(function () {
        var selectedVal = $(this).val();
        if (typeof(selectedVal) === 'undefined' || selectedVal === "") {
            layer.msg('Please choose subway line！', {icon: 5, time: 2000});

            return;
        }

       changeSubwayStation($subwayStation, selectedVal);
    });

    $(".uploaded-list-container a").hover(function () {
        $(this).append("<p id='pic'><img src='" + this.href + "' id='pic1'></p>");
        $(".uploaded-list-container a").mousemove(function (e) {
            $("#pic").css({
                "top": (e.pageY + 10) + "px",
                "left": (e.pageX + 20) + "px",
                "z-index": 100
            }).fadeIn("fast");
            // $("#pic").fadeIn("fast");
        });
    }, function () {
        $("#pic").remove();
    });

    $(".uploaded-list-container a").on('click', function () {
        var img = $(this),
            id = $(this).find("input").off().val(),
            target_house_id = $("#form-house-edit input[name='id']").val();
        //询问框
        layer.confirm('Operation', {
                btn: ['Set as cover', 'Delete', 'Cancel']
            }, function () {
                $.ajax({
                    type: 'POST',
                    url: '/admin/house/cover',
                    data: {'cover_id': id, 'target_id': target_house_id},
                    success: function (data) {
                        if (data.code === 200) {
                            layer.msg('Success', {icon: 1, time: 1000});
                        } else {
                            layer.msg('Failed！Reason: ' + data.message, {icon: 1, time: 3000});
                        }
                    },
                    error: function (jqXHR, textStatus, errorThrown) {
                        layer.msg('Failed！ Reason' + jqXHR.responseText, {icon: 1, time: 3000});

                    }
                });
            },
            function () {
                $.ajax({
                    type: 'DELETE',
                    url: '/admin/house/photo?id=' + id,
                    success: function (data) {
                        if (data.code === 200) {
                            img.remove();
                            layer.msg('Deleted', {icon: 1, time: 1000});
                        } else {
                            layer.msg('Failed！Reason: ' + data.message, {icon: 1, time: 3000});
                        }
                    },
                    error: function (jqXHR, textStatus, errorThrown) {
                        layer.msg('Failed！ Reason' + jqXHR.responseText, {icon: 1, time: 3000});

                    }
                });
            }, function () {
                layer.msg('Canceled', {icon: 1, time: 800});

            });
        return false;
    });

    //Form verification
    $("#form-house-edit").validate({
        rules: {
            id: {
                required: true
            },
            title: {
                required: true
            },
            cityEnName: {
                required: true,
            },
            regionEnName: {
                required: true,
            },
            street: {
                required: true
            },
            detailAddress: {
                required: true
            },
            room: {
                required: true
            },
            parlour: {
                required: true
            },
            floor: {
                required: true,
                digits: true,
                min: 1
            },
            totalFloor: {
                required: true,
                digits: true,
                min: 1
            },
            buildYear: {
                required: true,
                min: 1900
            },
            area: {
                required: true,
                min: 1
            },
            price: {
                required: true,
                number: true,
                min: 1
            },
            rentWay: {
                required: true,
                min: 0,
                max: 1
            },
            direction: {
                required: true
            }
        },
        // errorElement: 'span',
        messages: {
            city: 'Must choose city',
            region: 'Must choose region'
        },
          errorPlacement: function (error, element) {
             error.appendTo(element);
          },
        onkeyup: false,
        focusCleanup: true,
        success: "valid",
        submitHandler: function (form) {
            $(form).ajaxSubmit({
                type: 'post',
                url: '/admin/house/edit',
                success: function (data) {
                    if (data.code === 200) {
                        alert('Submit successful！');
                        parent_reload();
                        var index = parent.layer.getFrameIndex(window.name);
                        parent.$('.btn-refresh').click();
                        parent.layer.close(index);
                        removeIframe();
                    } else {
                        layer.msg(data.message, {icon: 5, time: 2000});
                    }
                },
                error: function (request, message, e) {
                    layer.msg(request.responseText, {icon: 5, time: 2000});
                }
            });
            return false; //return false block the form submiting
        }
    });
});