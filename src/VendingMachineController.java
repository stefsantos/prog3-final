public class VendingMachineController {
    private VendingMachineModel model;
    private MainMenu mainMenu;

    public VendingMachineController() {
        this.mainMenu = new MainMenu(this);
        this.mainMenu.setVisible(true);
    }

    public void showMainMenu() {
        mainMenu.setVisible(true);
    }

    public void createRegularVendingMachine(int numRows) {
        this.model = new VendingMachineModel(numRows);
        VendingMachineView vendingMachineView = new VendingMachineView(model);
        vendingMachineView.setVisible(true);
        vendingMachineView.updateBalanceLabel();
    }

    public void createSpecialVendingMachine(int numRows) {
        this.model = new VendingMachineModel(numRows);
        SpecialVendingMachineView specialVendingMachineView = new SpecialVendingMachineView(model);
        specialVendingMachineView.setVisible(true);
        specialVendingMachineView.updateBalanceLabel();
    }
}