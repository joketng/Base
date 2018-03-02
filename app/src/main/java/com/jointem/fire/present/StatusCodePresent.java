package com.jointem.fire.present;


import com.jointem.fire.IView.StatusCodeView;


public class StatusCodePresent {
    private StatusCodeView statusCodeView;

    public StatusCodePresent(StatusCodeView statusCodeView) {
        this.statusCodeView = statusCodeView;
    }

    public void getStatusCode(String url){
        /*APIStores.APIUrlStatusCode apiUrlStatusCode = RetrofitClient.getInstance().create(APIStores.APIUrlStatusCode.class);
        Call<ResponseBody> urlStatusCode = apiUrlStatusCode.getUrlStatusCode(url);
        urlStatusCode.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                if (response.isSuccessful()) {
                    statusCodeView.statusCodeSuccess();
                } else {
                    statusCodeView.statusCodeFailed(response.code());
                }
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                if (statusCodeView != null) {
                    statusCodeView.statusCodeFailed(404);
                }
            }
        });*/
    }
}
