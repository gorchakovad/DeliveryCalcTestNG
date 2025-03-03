import org.testng.Assert;
import org.testng.annotations.*;
import org.testng.asserts.SoftAssert;

import static org.testng.Assert.assertEquals;

public class DeliveryTests {

    priceCalculator calculator;

    @BeforeTest
    void init() {
        calculator = new priceCalculator();
    }

    @Test(groups = {"Positive", "Smoke"})
    public void deliveryCalcTest() {
        int minSum = 400;
        assertEquals(calculator.calcPrice(1.0, priceCalculator.Size.SMALL, false, priceCalculator.WorkloadList.NORMAL), minSum);
        assertEquals(calculator.calcPrice(50.0, priceCalculator.Size.BIG, false, priceCalculator.WorkloadList.VERY_HIGH_LOAD), 700);
        assertEquals(calculator.calcPrice(20.0, priceCalculator.Size.BIG, true, priceCalculator.WorkloadList.HIGHEST_LOAD), 1120);
    }

    @Test(dataProvider = "paramsMoreThan30", groups = {"Positive"})
    public void checkDistanceCalculatingMoreThan30(double distance) {
        assertEquals(calculator.distanceCalcPrice(distance), 300);
    }

    @DataProvider(name = "paramsMoreThan30")
    public Object[][] dataMethod1() {
        return new Object[][]{{30.01}, {31.0}, {500.0}};
    }

    @Test(dataProvider = "paramsLessThan30", groups = {"Positive"})
    public void checkDistanceCalculatingLessThan30(double distance) {
        assertEquals(calculator.distanceCalcPrice(distance), 200);
    }

    @DataProvider(name = "paramsLessThan30")
    public Object[][] dataMethod2() {
        return new Object[][]{{30.0}, {29.99}, {25.0}, {10.01}};
    }

    @Test(
            dataProvider = "paramsError",
            expectedExceptions = IllegalArgumentException.class,
            expectedExceptionsMessageRegExp = "Unknown distance value",
            groups = "Negative"
    )
    public void checkDistanceCalculationgErrors(double distance) {
        calculator = new priceCalculator();
        calculator.distanceCalcPrice(distance);
    }

    @DataProvider(name = "paramsError")
    public Object[][] paramsError() {
        return new Object[][]{{0.0}, {-0.01}, {-25.0}};
    }

    @Test(groups = {"Positive"})
    public void checkSize() {
        SoftAssert softAssert = new SoftAssert();
        priceCalculator.Size smallSize = priceCalculator.Size.SMALL;
        priceCalculator.Size bigSize = priceCalculator.Size.BIG;
        int smallSizePrice = calculator.sizeCalPrice(smallSize);
        int bigSizePrice = calculator.sizeCalPrice(bigSize);
        int smallPrice = 100;
        int bigPrice = 200;
        softAssert.assertEquals(smallSizePrice, smallPrice);
        softAssert.assertEquals(bigSizePrice, bigPrice);
        softAssert.assertAll();
    }

    @Test(groups = {"Positive"})
    void checkWorkload() {
        assertEquals(calculator.Workload(priceCalculator.WorkloadList.NORMAL), 1.0);
        assertEquals(calculator.Workload(priceCalculator.WorkloadList.HIGH_LOAD), 1.2);
        assertEquals(calculator.Workload(priceCalculator.WorkloadList.VERY_HIGH_LOAD), 1.4);
        assertEquals(calculator.Workload(priceCalculator.WorkloadList.HIGHEST_LOAD), 1.6);
    }

    @Test(groups = {"Positive"})
    void checkFragile() {
        assertEquals(calculator.Fragility(true), 300);
        assertEquals(calculator.Fragility(false), 0);
    }

    @Test(
            expectedExceptions = IllegalArgumentException.class,
            expectedExceptionsMessageRegExp = "We can't deliver fragile cargos for more than 30km",
            groups = {"Negative", "Smoke"}
    )
    void checkFragileAndMoreThan30km() {
        calculator = new priceCalculator();
        calculator.calcPrice(35.0, priceCalculator.Size.SMALL, true, priceCalculator.WorkloadList.NORMAL);
    }
}
