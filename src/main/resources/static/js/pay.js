async function requestPayment() {
	const payMethod = document.querySelector('meta[name="payMethod"]').getAttribute('content');
	const easyPayProvider = document.querySelector('meta[name="easyPayProvider"]')?.getAttribute('content');
	const orderName = document.querySelector('meta[name="orderName"]').getAttribute('content');
	const totalAmount = document.querySelector('meta[name="totalAmount"]').getAttribute('content');
	const storeId = document.querySelector('meta[name="storeId"]').getAttribute('content');
	const channelKey = document.querySelector('meta[name="channelKey"]').getAttribute('content');
	const joinId = document.querySelector('meta[name="joinId"]').getAttribute('content');

	let response;

	if (payMethod === "EASY_PAY") {
		response = await PortOne.requestPayment({
			// Store ID 설정
			storeId: "store-603182ab-7328-4075-80f7-188e067089fe",
			// 채널 키 설정
			channelKey: "channel-key-458617ca-ce6c-4d64-a119-148b131b13fc",
			paymentId: `payment-${crypto.randomUUID()}`,
			orderName: orderName,
			totalAmount: parseInt(totalAmount),
			currency: "CURRENCY_KRW",
			payMethod: payMethod,
			easyPay: { easyPayProvider: easyPayProvider }
		});
	}
	else {
		response = await PortOne.requestPayment({
			// Store ID 설정
			storeId: "store-603182ab-7328-4075-80f7-188e067089fe",
			// 채널 키 설정
			channelKey: "channel-key-458617ca-ce6c-4d64-a119-148b131b13fc",
			paymentId: `payment-${crypto.randomUUID()}`,
			orderName: orderName,
			totalAmount: parseInt(totalAmount),
			currency: "CURRENCY_KRW",
			payMethod: payMethod
		});
	}

	if (response.code != null) {
		// 오류 발생
		return alert(response.message);
	}


	

	const notified = await fetch("http://localhost/pay/complete", {
		method: "POST",
		headers: { "Content-Type": "application/json" },
		body: JSON.stringify({
			paymentId: response.paymentId,
			joinId: joinId,
			orderName: orderName,
			totalAmount: totalAmount
		}),
	}).then(response=> {
		if(!response.ok) throw Error ("에러 발생함")
		else if(response.ok) console.log("200 ok")
		else console.log(response.status)
		return response.text();
	}).then(data=>{
		console.log(data);
		window.location.href =  "../../" + data; // 페이지 이동 pay/result
	})
	.catch(error=>{
		console.error("에러 캐치 : " + error);
	});
}

