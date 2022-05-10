package br.com.caelum.camel;

import org.apache.camel.CamelContext;
import org.apache.camel.builder.RouteBuilder;
import org.apache.camel.impl.DefaultCamelContext;

public class RotaPedidos {

	public static void main(String[] args) throws Exception {

		CamelContext context = new DefaultCamelContext();
		context.addRoutes(new RouteBuilder() {
			
			@Override
			public void configure() throws Exception {
				
				//Copia os Arquivos da Pasta Pedidos para a Pasta Saida
				//Noop mantem o Arquivo na Pasta Pedidos
				//Se Retirar ou Colocar false e Movido os Arquivos
				// SetHeader Altera o Nome do Arquivo
				
				from("file:pedidos?delay=5s&noop=true")
				.split().xpath("/pedido/itens/item")
				.filter().xpath("/item/formato[text()='EBOOK']")
				.log("${id} - ${body}")
				.marshal().xmljson()
				.log("${body}")
				.setHeader("CamelFileName", simple("${file:name.noext}.json"))
				.to("file:saida");
				
			}
		});
		
		context.start();
		Thread.sleep(20000);
		context.stop();

	}	
}
