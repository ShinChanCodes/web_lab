package com.cnsi.asonetaskcreator;

import java.io.IOException;
import java.io.PrintWriter;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * Servlet implementation class AsOneTaskCreatorServlet
 */
@WebServlet("/AsOneTaskCreatorServlet")
public class AsOneTaskCreatorServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public AsOneTaskCreatorServlet() {
        super();
        // TODO Auto-generated constructor stub
    }

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		doPost(request,response);
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		response.setContentType("text/html;charset=UTF-8");
		
		PrintWriter Out=response.getWriter();
		//Out.println("<h2>WELCOME!</h2><p>Have a great Day</p>");
		
		String webINFRootDir = request.getServletContext().getRealPath("/WEB-INF");
		
		String txtIterationNo = request.getParameter("txtIterationNo");
		String txtIterationStartDate = request.getParameter("txtIterationStartDate");
		String txtIterationEndDate = request.getParameter("txtIterationEndDate");
		String txtCQEstimationFile = request.getParameter("txtCQEstimationFile");
			
        AsOneTaskCreator asOneTaskCreator1 = new AsOneTaskCreator();
       
        asOneTaskCreator1.loadEstimationSheetVOs(txtCQEstimationFile);
       
       
        asOneTaskCreator1.processAsOneTaskCreator(webINFRootDir, txtIterationNo, txtIterationStartDate, txtIterationEndDate, txtCQEstimationFile);//error-2
	
        Out.println("<h2>Hello</h2><p>It Is Generated!!!!!!!!</p>");
	}
}
