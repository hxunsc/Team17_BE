function getRequestWithToken(uri) {

  fetch(uri, {
    method: 'GET',
    headers: {
      'Authorization': `Bearer ${localStorage.getItem('access_token')}`
    }
  })
  .then(response => {
    if (!response.ok) {
      throw new Error(JSON.stringify(response));
    }
    return response.text();
  })
  .then(html => {
    document.open();
    document.write(html);
    document.close();
    window.history.pushState({}, '', uri);
  })
  .catch(error => {
    if (error.errorCode === '401') {
      alert('회원이 아닙니다.');
      localStorage.removeItem('access_token');
    } else {
      alert('확인되지 않은 에러입니다. 관리자에게 연락해주시기 바랍니다.');
      console.log(error);
    }
  });
}
