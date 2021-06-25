public class NBody {

	public static double readRadius(String fileName){
		In in = new In(fileName);
		int n = in.readInt();
		double radius = in.readDouble();
		return radius;
	}

	public static Planet[] readPlanets(String fileName){
		In in = new In(fileName);
		int n = in.readInt();
		double radius = in.readDouble();
		Planet[] allPlanets = new Planet[n];
		int i = 0;
		while(i < n){
			double xxPos = in.readDouble();
			double yyPos = in.readDouble();
			double xxVel = in.readDouble();
			double yyVel = in.readDouble();
			double mass = in.readDouble();
			String imgFileName = in.readString();
			allPlanets[i] = new Planet(xxPos, yyPos ,xxVel, yyVel, mass, imgFileName);
			i += 1;
		}
		return allPlanets;
	}

	public static void main(String[] args) {
		double T = Double.parseDouble(args[0]);
		double dt = Double.parseDouble(args[1]);
		String filename = args[2];
		double radius = NBody.readRadius(filename);
		Planet[] allPlanets = NBody.readPlanets(filename);
		int n = allPlanets.length;

		StdDraw.enableDoubleBuffering();
		StdDraw.setScale(-radius, radius);
		StdDraw.clear();

		double time = 0;
		while(time < T){
			double[] xForces = new double[n];
			double[] yForces = new double[n];
			for (int i = 0; i < n; i++) {
				xForces[i] = allPlanets[i].calcNetForceExertedByX(allPlanets);
				yForces[i] = allPlanets[i].calcNetForceExertedByY(allPlanets);
			}
			for (int i = 0; i < allPlanets.length; i++) {
				allPlanets[i].update(dt, xForces[i], yForces[i]);
			}
			StdDraw.picture(0, 0, "images/starfield.jpg");
			for (Planet p : allPlanets) {
				p.draw();
			}
			StdDraw.show();
			StdDraw.pause(10);
			time += dt;
		}

		StdOut.printf("%d\n", n);
		StdOut.printf("%.2e\n", radius);
		for (int i = 0; i < n; i++) {
    		StdOut.printf("%11.4e %11.4e %11.4e %11.4e %11.4e %12s\n",
                  allPlanets[i].xxPos, allPlanets[i].yyPos, allPlanets[i].xxVel,
                  allPlanets[i].yyVel, allPlanets[i].mass, allPlanets[i].imgFileName);   
		}
	}
}