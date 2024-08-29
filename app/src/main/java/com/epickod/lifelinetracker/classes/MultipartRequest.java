package com.epickod.lifelinetracker.classes;


import com.android.volley.AuthFailureError;
import com.android.volley.NetworkResponse;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyLog;
import com.android.volley.toolbox.HttpHeaderParser;

import java.io.ByteArrayOutputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.util.Map;

public class MultipartRequest extends Request<String> {

    private final String boundary = "apiclient-" + System.currentTimeMillis();
    private final String mimeType = "multipart/form-data;boundary=" + boundary;
    private final Response.Listener<String> listener;
    private final Map<String, String> headers;
    private final Map<String, String> params;
    private final byte[] fileData;
    private final String fileName;

    public MultipartRequest(String url, Map<String, String> params, byte[] fileData, String fileName,
                            Response.Listener<String> listener, Response.ErrorListener errorListener) {
        super(Method.POST, url, errorListener);
        this.listener = listener;
        this.headers = null;
        this.params = params;
        this.fileData = fileData;
        this.fileName = fileName;
    }

    @Override
    public String getBodyContentType() {
        return mimeType;
    }

    @Override
    public byte[] getBody() throws AuthFailureError {
        ByteArrayOutputStream bos = new ByteArrayOutputStream();
        DataOutputStream dos = new DataOutputStream(bos);
        try {
            // Write form fields
            for (Map.Entry<String, String> entry : params.entrySet()) {
                buildTextPart(dos, entry.getKey(), entry.getValue());
            }
            // Write file data
            buildFilePart(dos, "prescription_photo", fileData, fileName);
            // End of multipart/form-data
            dos.writeBytes("--" + boundary + "--\r\n");
        } catch (IOException e) {
            VolleyLog.e("IOException writing to ByteArrayOutputStream");
        } finally {
            try {
                dos.flush();
                dos.close();
            } catch (IOException ignored) {
            }
        }
        return bos.toByteArray();
    }

    @Override
    protected Response<String> parseNetworkResponse(NetworkResponse response) {
        return Response.success(new String(response.data), HttpHeaderParser.parseCacheHeaders(response));
    }

    @Override
    protected void deliverResponse(String response) {
        listener.onResponse(response);
    }

    private void buildTextPart(DataOutputStream dos, String parameterName, String parameterValue) throws IOException {
        dos.writeBytes("--" + boundary + "\r\n");
        dos.writeBytes("Content-Disposition: form-data; name=\"" + parameterName + "\"\r\n\r\n");
        dos.writeBytes(parameterValue + "\r\n");
    }

    private void buildFilePart(DataOutputStream dos, String inputName, byte[] fileData, String fileName) throws IOException {
        dos.writeBytes("--" + boundary + "\r\n");
        dos.writeBytes("Content-Disposition: form-data; name=\"" + inputName + "\"; filename=\"" + fileName + "\"\r\n");
        dos.writeBytes("Content-Type: image/jpeg\r\n\r\n");
        dos.write(fileData);
        dos.writeBytes("\r\n");
    }

    @Override
    public Map<String, String> getHeaders() throws AuthFailureError {
        return headers != null ? headers : super.getHeaders();
    }
}
