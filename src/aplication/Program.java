package aplication;

import java.text.ParseException;
import java.text.SimpleDateFormat;

import model.dao.DaoFactory;
import model.dao.SellerDao;
import model.entities.Department;
import model.entities.Seller;

public class Program {

	public static void main(String[] args) throws ParseException {
		SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
		SellerDao sellerdao = DaoFactory.creatSellerDao();
		
		/*Seller seller = sellerdao.findById(3);
		System.out.println(seller);
		System.out.println();
		
		List<Seller>list = sellerdao.finAll();
		
		list.forEach(System.out::println);*/
		
		
	 sellerdao.insertSeller(new Seller(null, "Jose", "jose@hotmail.com", sdf.parse("10/08/1989"), 1500.0, new Department(2, null)));
		
		
		
		
		
		
	}

}
