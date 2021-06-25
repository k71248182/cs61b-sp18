public class Planet {
	
	public double xxPos;
	public double yyPos;
	public double xxVel;
	public double yyVel;
	public double mass;
	public String imgFileName;

	static final double g = 6.67e-11;

	public Planet(double xP, double yP, double xV,
              double yV, double m, String img){
		xxPos = xP;
		yyPos = yP;
		xxVel = xV;
		yyVel = yV;
		mass = m;
		imgFileName = img;
	}

	public Planet(Planet p){
		this.xxPos = p.xxPos;
		this.yyPos = p.yyPos;
		this.xxVel = p.xxVel;
		this.yyVel = p.yyVel;
		this.mass = p.mass;
		this.imgFileName = p.imgFileName;
	}

	public double calcDistance(Planet p){
		double dx = (this.xxPos - p.xxPos);
		double dy = (this.yyPos - p.yyPos);
		double r = Math.sqrt(dx*dx + dy*dy);
		return r;
	}

	public double calcForceExertedBy(Planet p){
		double r = this.calcDistance(p);
		double f = g * this.mass * p.mass/(r * r);
		return f;
	}

	public double calcForceExertedByX(Planet p){
		double dx = p.xxPos - this.xxPos;
		double f = this.calcForceExertedBy(p);
		double r = this.calcDistance(p);
		double fx = f * dx / r;
		return fx;
	}

	public double calcForceExertedByY(Planet p){
		double dy = p.yyPos - this.yyPos;
		double f = this.calcForceExertedBy(p);
		double r = this.calcDistance(p);
		double fy = f * dy / r;
		return fy;
	}

	public double calcNetForceExertedByX(Planet[] allPlanets){
		double sumFx = 0;
		for(Planet p : allPlanets){
			if (p.equals(this) == false) {
				sumFx += this.calcForceExertedByX(p);	
			}
		}
		return sumFx;
	}

	public double calcNetForceExertedByY(Planet[] allPlanets){
		double sumFy = 0;
		for(Planet p : allPlanets){
			if (p.equals(this) == false) {
				sumFy += this.calcForceExertedByY(p);	
			}
		}
		return sumFy;
	}

	public void update(double dt, double fX, double fY){
		double aX = fX / this.mass;
		double aY = fY / this.mass;
		this.xxVel += dt * aX;
		this.yyVel += dt * aY;	
		this.xxPos += dt * this.xxVel;	
		this.yyPos += dt * this.yyVel;
	}

	public void draw(){
		StdDraw.picture(xxPos, yyPos, "images/" + imgFileName);
	}
}









