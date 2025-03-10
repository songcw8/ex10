package org.example.controller;

import io.github.cdimascio.dotenv.Dotenv;
import jakarta.servlet.ServletException;
import jakarta.servlet.ServletOutputStream;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.model.APIParam;
import org.service.APIService;

import java.io.IOException;
import java.util.logging.Logger;

@WebServlet(name = "APIServlet", value = "/api")
public class APIController extends HttpServlet {
    //싱글톤 패턴을 기반으로 한 의존성 주입
    final APIService apiService = APIService.getInstance();
    final Logger logger = Logger.getLogger(APIController.class.getName());
    @Override
    public void init() throws ServletException {
        logger.info("APIController init...");
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        // ?prompt = 점메추
        String prompt = req.getParameter("prompt");
        String model = req.getParameter("model");
        resp.setContentType("application/json");
        resp.setCharacterEncoding("UTF-8");
        ServletOutputStream out = resp.getOutputStream();
        APIParam apiParam = new APIParam(prompt,model);
        out.println(apiService.callAPI(apiParam));
    }
}
