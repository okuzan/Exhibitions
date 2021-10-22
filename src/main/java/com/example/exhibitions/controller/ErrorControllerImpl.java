//package com.example.exhibitions.controller;
//
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.boot.web.servlet.error.ErrorAttributes;
//import org.springframework.boot.web.servlet.error.ErrorController;
//import org.springframework.http.HttpStatus;
//import org.springframework.stereotype.Controller;
//import org.springframework.web.bind.annotation.RequestMapping;
//import org.springframework.web.bind.annotation.ResponseBody;
//import org.springframework.web.context.request.ServletWebRequest;
//
//import javax.servlet.RequestDispatcher;
//import javax.servlet.http.HttpServletRequest;
//import java.util.Map;
//
//@Controller
//public class ErrorControllerImpl implements ErrorController {
//
//    @Autowired
//    private ErrorAttributes errorAttributes;
//
//    private static final String PATH = "/error";
//
////    @RequestMapping(value = PATH)
////    public String error() {
////        return "views/all/not_found";
////    }
//
//    @RequestMapping("/error")
//    public String handleError(HttpServletRequest request) {
//        // get error status
//        Object status = request.getAttribute(RequestDispatcher.ERROR_STATUS_CODE);
//        System.out.println(status);
//        Object status2 = request.getAttribute(RequestDispatcher.ERROR_MESSAGE);
//        System.out.println(status2);
//        Object status3 = request.getAttribute(RequestDispatcher.ERROR_EXCEPTION);
//        System.out.println(status3);
//
//        // TODO: log error details here
//
//        if (status != null) {
//            int statusCode = Integer.parseInt(status.toString());
//
//            // display specific error page
//            if (statusCode == HttpStatus.NOT_FOUND.value()) {
//                return "error/404";
//            } else if (statusCode == HttpStatus.INTERNAL_SERVER_ERROR.value()) {
//                return "error/500";
//        }}
//
//        // display generic error
//        return "error";
//    }
//
//
////    @RequestMapping("/error")    //mandatory mapping
////    public @ResponseBody
////    String handleError(HttpServletRequest req) {
////
////        ServletWebRequest servletWebRequest = new ServletWebRequest(req);
////
////        @SuppressWarnings("deprecation")
////        Map<String, Object> errors = errorAttributes.getErrorAttributes(servletWebRequest,null);
////
////        StringBuilder builder = new StringBuilder();
////        builder.append("<html><body>");
////        builder.append("<h2>ERROR SUMMARY</h2>");
////
////        builder.append("<table border='1.5'>");
////        errors.forEach((key, value) -> {
////            builder.append("<tr>").append("<td>").append(key).append("</td>").append("<td>").append(value).append("</td>")
////                    .append("</tr>");
////        });
////        builder.append("</table>");
////        builder.append("</body></html>");
////        return builder.toString();
////    }
//
//    @RequestMapping("/denied")
//    public String access() {
//        return "views/all/denied";
//    }
//}
