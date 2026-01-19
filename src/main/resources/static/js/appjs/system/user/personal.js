let prefix = "/sys/user"

$(function () {
    utils.createDatePicker('birth', {});
});

/**
 * 基本信息提交
 */
$("#base_save").click(function () {
    let hobbyStr = getHobbyStr();
    $("#hobby").val(hobbyStr);
    if ($("#basicInfoForm").valid()) {
        $.ajax({
            cache: true,
            type: "POST",
            url: "/sys/user/updatePersonal",
            data: $('#basicInfoForm').serialize(),
            async: false,
            error: function (request) {
                laryer.alert("Connection error");
            },
            success: function (data) {
                if (data.code === 0) {
                    parent.layer.msg("更新成功");
                } else {
                    parent.layer.alert(data.msg)
                }
            }
        });
    }

});
$("#pwd_save").click(function () {
    if ($("#modifyPwd").valid()) {
        $.ajax({
            cache: true,
            type: "POST",
            url: "/sys/user/resetPwd",
            data: $('#modifyPwd').serialize(),
            async: false,
            error: function (request) {
                parent.laryer.alert("Connection error");
            },
            success: function (data) {
                if (data.code === 0) {
                    parent.layer.alert("更新密码成功");
                    $("#photo_info").click();
                } else {
                    parent.layer.alert(data.msg)
                }
            }
        });
    }
});

function getHobbyStr() {
    return $(".hobby:checked").map(function() {
        return this.value;
    }).get().join(',');
}