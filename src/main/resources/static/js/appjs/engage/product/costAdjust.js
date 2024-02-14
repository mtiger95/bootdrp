$().ready(function() {
	validateRule();
	utils.loadTypes(["data_shop"], ["shopNo"], [{width: "100%"}]);
});

function validateRule() {
	$("#signupForm").validate({
		submitHandler: function () {
			update();
		}
	})
}

function update() {
	$.ajax({
		cache : true,
		type : "POST",
		url : "/engage/product/cost/adjust",
		data : $('#signupForm').serialize(),// 你的formid
		async : false,
		error : function(request) {
			parent.layer.alert("Connection error");
		},
		success : function(data) {
			if (data.code === 0) {
				parent.layer.msg("操作成功");
				parent.search();
				let index = parent.layer.getFrameIndex(window.name); // 获取窗口索引
				parent.layer.close(index);
			} else {
				parent.layer.alert(data.msg)
			}
		}
	});
}