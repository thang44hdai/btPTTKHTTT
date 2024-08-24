package com.company.servlet;

import java.io.IOException;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import com.company.dao.BookDao;
import com.company.dao.BookDaoImpl;
import com.company.bean.Book;
import java.util.List;

@WebServlet("/BookServlet")
public class BookServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
	private static BookDao bookDao = new BookDaoImpl();

	public BookServlet() {
		super();
	}

	protected void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		String action = request.getParameter("action");

		if (action == null) {
			action = "list";
		}

		switch (action) {
		case "new":
			request.getRequestDispatcher("addBook.jsp").forward(request, response);
			break;
		case "edit":
			showEditForm(request, response);
			break;
		case "delete":
			deleteBook(request, response);
			break;
		case "list":
		default:
			listBooks(request, response);
			break;
		}
	}

	private void listBooks(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		List<Book> listBooks = bookDao.selectAllBooks();
		request.setAttribute("listBooks", listBooks);
		request.getRequestDispatcher("welcome.jsp").forward(request, response);
	}

	private void showEditForm(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		int id = Integer.parseInt(request.getParameter("id"));
		Book existingBook = bookDao.selectBook(id);
		request.setAttribute("book", existingBook);
		request.getRequestDispatcher("editBook.jsp").forward(request, response);
	}

	private void deleteBook(HttpServletRequest request, HttpServletResponse response) throws IOException {
		int id = Integer.parseInt(request.getParameter("id"));
		bookDao.deleteBook(id);
		response.sendRedirect("BookServlet");
	}

	@Override
	protected void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		String action = request.getParameter("action");

		if (action == null) {
			response.sendRedirect("BookServlet");
			return;
		}

		switch (action) {
		case "insert":
			insertBook(request, response);
			break;
		case "update":
			updateBook(request, response);
			break;
		}
	}

	private void insertBook(HttpServletRequest request, HttpServletResponse response) throws IOException {
		String title = request.getParameter("title");
		String author = request.getParameter("author");
		double price = Double.parseDouble(request.getParameter("price"));

		Book newBook = new Book();
		newBook.setTitle(title);
		newBook.setAuthor(author);
		newBook.setPrice(price);

		bookDao.insertBook(newBook);
		response.sendRedirect("BookServlet");
	}

	private void updateBook(HttpServletRequest request, HttpServletResponse response) throws IOException {
		int id = Integer.parseInt(request.getParameter("id"));
		String title = request.getParameter("title");
		String author = request.getParameter("author");
		double price = Double.parseDouble(request.getParameter("price"));

		Book book = new Book();
		book.setId(id);
		book.setTitle(title);
		book.setAuthor(author);
		book.setPrice(price);

		bookDao.updateBook(book);
		response.sendRedirect("BookServlet");
	}
}
