$(function () {

    var $city = $("#city"),
        $region = $("#region"),
        $subwayLine = $("#subwayLine"),
        $subwayStation = $("#subwayStation");

    changeCity($city);

    $city.change(function () {
        var selectedVal = $(this).val();
        if (typeof(selectedVal) == 'undefined' || selectedVal == "") {
            layer.msg('Please choose the city！', {icon: 5, time: 2000});
            return;
        }

        changeRegion($region, selectedVal);
        changeSubwayLine($subwayLine, selectedVal);
    });

    $subwayLine.change(function () {
        var selectedVal = $(this).val();
        if (typeof(selectedVal) == 'undefined' || selectedVal == "") {
            layer.msg('Please choose the subway！', {icon: 5, time: 2000});
            return;
        }

        changeSubwayStation($subwayStation, selectedVal);
    });

    var tags = new Set();
    $('#tags span').on('click', function () {
       var tag = $(this).text();
       if (tags.has(tag)) {
           $(this).removeClass('label-success').addClass('label-default').css('border', 'none');
           tags.delete(tag);
       } else {
           $(this).removeClass('label-default').addClass('label-success').css('border', 'solid black 1px');
           tags.add(tag);
       }
    });

    //Form verification
    $("#form-house-add").validate({
        rules: {
            title: {
                required: true,
                maxlength: 30
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
            address: {
                required: true,
                maxlength: 30
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
        messages: {
            province: 'Must choose province',
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
            var cover = $(form).find("input:radio[name='cover']:checked").val();

            if (cover == null || typeof(cover) == "undefined" || cover == "" || cover.length < 1) {
                layer.msg('Must choose the image cover！', {icon: 5, time: 2000});
                return false;
            }

            $(form).find('input.house-tag').remove();
            var index = 0;
            tags.forEach(function (tag) {
               $(form).append('<input class="house-tag" name="tags[' + index++ + ']" type="hidden" value="'+ tag + '"/>');
            });

            $(form).ajaxSubmit({
                type: 'post',
                url: '/admin/add/house', //
                success: function (data) {
                    if (data.code === 200) {
                        alert('Submit successfully！');
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
            return false;//block the submit
        }
    });
});
