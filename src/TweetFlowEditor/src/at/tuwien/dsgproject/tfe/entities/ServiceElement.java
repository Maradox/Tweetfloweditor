package at.tuwien.dsgproject.tfe.entities;

import android.graphics.Canvas;

public class ServiceElement extends AbstractElement {
	
	ServiceElement(int id, int x, int y, int width, int height) {
		super(id, x, y, width, height);
		// TODO Auto-generated constructor stub
	}

	ServiceElement(int id, int x, int y, int width, int height, double scale) {
		super(id, x, y, width, height, scale);
		// TODO Auto-generated constructor stub
	}

	@Override
	public void draw(Canvas canvas) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void scale(float scaleFactor) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public boolean contains(int x, int y) {
		// TODO Auto-generated method stub
		return false;
	}
}
