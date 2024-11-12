document.getElementById("editProductForm").addEventListener("submit", function(event) {
  event.preventDefault(); // 기본 폼 제출 방지

  // 폼에서 필요한 데이터 가져오기
  const productId = document.getElementById("productId").value;
  const requestJson = {
    "name": document.getElementById("name").value,
    "price": parseInt(document.getElementById("price").value, 10),
    "storeName": document.getElementById("storeName").value,
    "tagId": parseInt(document.getElementById("tagId").value, 10)
  };

  // PUT 요청 보내기
  $.ajax({
    type: 'PUT',
    url: `/admin/page/product/edit/${productId}`,
    contentType: 'application/json; charset=utf-8',
    data: JSON.stringify(requestJson),
    beforeSend: function (xhr) {
      xhr.setRequestHeader('Authorization', "Bearer " + localStorage.getItem('access_token'));
    },
    success: function () {
      alert('상품이 수정되었습니다.');
      getRequestWithToken('/admin/page/product'); // 상품 목록 페이지로 돌아가기
    },
    error: function (xhr) {
      const errorResponse = xhr.responseJSON;
      alert(errorResponse.message);  // 에러 메시지 alert
    }
  });
});