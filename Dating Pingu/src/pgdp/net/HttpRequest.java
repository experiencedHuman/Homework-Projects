package pgdp.net;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.IntStream;

public final class HttpRequest {

    private HttpMethod method;
    private String path;
    private Map<String, String> parameters;

    public HttpRequest(String request) {
        if (request == null || request.isEmpty())
            throw new UnsupportedRequestException("Empty Request!");
        List<String> data = List.of(request.split(" "));
        try {
            method = (data.get(0).equals("GET")) ? HttpMethod.GET : (data.get(0).equals("POST") ? HttpMethod.POST : null);
            if (method == null || method == HttpMethod.POST) throw new UnsupportedMethodException("Unsupported Method!");
                findParameters(data.get(1));
        } catch (NullPointerException | ArrayIndexOutOfBoundsException e) {
            throw new UnsupportedRequestException("Unsupported Request");
        }
    }

    private void findParameters(String path) {
        if (parameters == null)
            parameters = new LinkedHashMap<>();

        List<String> parameters = List.of(path.split("\\?"));
        if (!parameters.isEmpty()) {
            this.path = parameters.get(0);
            boolean moreThanTwoParameters = parameters.size() >= 3;
            boolean onlyOneParameter = parameters.size() == 1;

            if (moreThanTwoParameters)
                throw new UnsupportedRequestException();
            if (onlyOneParameter)
                return;
        } else return;

        List<String> p = List.of(parameters.get(1).split("&"));
        IntStream.range(0, p.size()).forEach(i -> {
            String params = p.get(i);
            int equal = params.indexOf("=");
            String dataType = params.substring(0, equal);
            String dataValue = params.substring(equal + 1);
            if (dataType.isEmpty() || dataValue.isEmpty())
                throw new UnsupportedRequestException();
            this.parameters.put(dataType, dataValue);
        });

    }

    public HttpMethod getMethod() {
        return method;
    }

    public String getPath() {
        return path;
    }

    public Map<String, String> getParameters() {
        return parameters;
    }
}
