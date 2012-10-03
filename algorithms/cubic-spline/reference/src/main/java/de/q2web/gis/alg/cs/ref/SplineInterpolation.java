package de.q2web.gis.alg.cs.ref;

@Deprecated
public class SplineInterpolation {

	public void compute(final float[] x, final float[] y, final int length) {

		// starts at 0
		// int gid = get_global_id(0);

		// f(x) = a + bx +cx^2 +dx^3
		// f'(x) = b + 2cx +3dx^2
		// f''(x) = 2c +6x

		// §1 Each curve segment passes through its control points, thus
		// f_i(x_i) = y_i, f_{i+1}(x_{i+1}) = y_{i+1}
		// I a_i + b_ix_i +c_ix^2_i +d_ix^3_i = y_i
		// II a_i + b_ix_{i+1} +C_1ix^2_{i+1} +d_ix^3_{i+1} = y_i

		// §2 Each curve segment has the same slope where they join, thus
		// f'_i(x_{i+1}) = f'_{i+1}}(x_{i+1})
		// enforcing C_1 continuity
		// III b_i + 2c_ix_{i+1} + 3d_ix^2_{i+1} - b_{i+1} - 2c_{i+}}x_{i+1} +
		// 3d_{i+1}}x^2_{i+1} = 0

		// §3 Each curve segment has the same curvature where they join, thus
		// f''_i(x_{i+1}) = f''_{i+1}}(x_{i+1})
		// enforcing C_2 continuity
		// IV 2c_i + 6d_ix_{i+1} - 2c_{i+1} - 6d_{i+1}x_{i+1} = 0

		// $4 At the left end of the curve we are missing the C_1 and C_2
		// equations

		// =>

		float x0 = 0, y0 = 0, xa, ya, xb, yb, xc, yc;
		float fPrimeLeft;
		float fPrimeRight;
		float fDoublePrimeLeft;
		float fDoublePrimeRight;
		final float d[] = new float[length]; // d[0] is left empty for better
		// readibility
		final float c[] = new float[length]; // c[0] is left empty for better
		// readibility
		final float b[] = new float[length]; // b[0] is left empty for better
		// readibility
		final float a[] = new float[length]; // a[0] is left empty for better
		// readibility

		// f(xx) = yy
		// yy = a[i] + b[i] * xx + c[i] + xx*xx + d[i] + xx*xx*xx;
		int i; // control point (start with 0)
		for (i = 1; i < length - 1; i++) {
			if (i != 1) {
				x0 = x[i - 2];
				y0 = y[i - 2];
			}
			xa = x[i - 1];
			ya = y[i - 1];
			xb = x[i];
			yb = y[i];
			xc = x[i + 1];
			yc = y[i + 1];

			fPrimeRight = 2 / ((xc - xb) / (yc - yb) + (xb - xa) / (yb - ya));

			fPrimeLeft = i != 1 ? 2 / ((xb - xa) / (yb - ya) + (xa - x0)
					/ (ya - y0)) : 3f / 2f * (yb - ya) / (xb - xa)
					- fPrimeRight / 2;

			if (i == length) {
				fPrimeRight = 3f / 2f * ((yb - ya) / (xb - xa)) - fPrimeLeft
						/ 2;
			}

			fDoublePrimeLeft = -(2 * (fPrimeRight + 2 * fPrimeLeft))
					/ (xb - xa) + (6 * (yb - ya) / (xb - xa)) / (xb - xa);
			fDoublePrimeRight = 2 * (2 * fPrimeRight + fPrimeLeft) / (xb - xa)
					- 6 * (yb - ya) / (xb - xa) / (xb - xa);

			d[i] = 1f / 6f * (fDoublePrimeRight - fDoublePrimeLeft) / (xb - xa);
			c[i] = 1f / 2f * (xb * fDoublePrimeLeft - xa * fDoublePrimeRight)
					/ (xb - xa);
			b[i] = ((yb - ya) - c[i] * (xb * xb - xa * xa) - d[i]
					* (xb * xb * xb - xa * xa * xa))
					/ (xb - xa);
			a[i] = ya - b[i] * xa - c[i] * xa * xa - d[i] * xa * xa * xa;
		}
	}

}
