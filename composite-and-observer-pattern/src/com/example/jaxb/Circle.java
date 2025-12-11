package com.example.jaxb;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement
public class Circle {
	private int radius, x, y;

	public Circle() {
	}

	public Circle(int radius, int x, int y) {
		this.radius = radius;
		this.x = x;
		this.y = y;
	}

	@XmlAttribute(name = "yarıçap", required = true)
	public int getRadius() {
		return radius;
	}

	public void setRadius(int radius) {
		this.radius = radius;
	}

	@XmlAttribute()
	public int getX() {
		return x;
	}

	public void setX(int x) {
		this.x = x;
	}

	@XmlAttribute()
	public int getY() {
		return y;
	}

	public void setY(int y) {
		this.y = y;
	}

	public static void main(String[] args) {
		try {
			JAXBContext jc = JAXBContext.newInstance(Circle.class);
			Marshaller m = jc.createMarshaller();
			m.marshal(new Circle(1, 2, 10), System.out);
		} catch (JAXBException jbe) {
			jbe.printStackTrace();
		}
	}

}
