import { Route, Routes } from "react-router-dom";

import AccountManager from "../pages/admin/AccountManager";
import AccountMarket from "../pages/admin/AccountMarket";
import BillDetail from "../pages/admin/BillDetail";
import BillDetailEdit from "../pages/manager/BillDetailEdit";
import BillDetailEditMarket from "../pages/supermarket/BillDetailEditMarket";
import BillDetailManager from "../pages/manager/BillDetailManager";
import BillDetailMarket from "../pages/supermarket/BillDetailMarket";
import Bills from "../pages/admin/Bills";
import Category from "../pages/admin/Category";
import ChartExportAndImport from "../pages/manager/ChartExportAndImport";
import ChartExportAndImportMarket from "../pages/supermarket/ChartExportAndImportMarket";
import ChartOverviewBill from "../pages/admin/ChartOverviewBill";
import CreateBillManager from "../pages/manager/CreateBillManager";
import CreateBillMarket from "../pages/supermarket/CreateBillMarket";
import DashBoardAdmin from "../pages/admin/DashBoardAdmin";
import DashboardManager from "../pages/manager/DashboardManager";
import DashboardMarket from "../pages/supermarket/DashboardMarket";
import DetailList from "../pages/admin/DetailList";
import DetailListManager from "../pages/manager/DetailListManager";
import DetailListMarket from "../pages/supermarket/DetailListMarket";
import DoughtnutRevenue from "../pages/admin/DoughtnutRevenue";
import ExportBill from "../pages/manager/ExportBill";
import ExportBillMarket from "../pages/supermarket/ExportBillMarket";
import ImportBill from "../pages/manager/ImportBill";
import ImportBillMarket from "../pages/supermarket/ImportBillMarket";
import IndexAdmin from "../pages/admin/IndexAdmin";
import IndexManager from "../pages/manager/IndexManager";
import IndexSuperMarket from "../pages/supermarket/IndexSuperMarket";
import InformationManager from "../pages/information/InformationManager";
import InformationMarket from "../pages/information/InformationMarket";
import InventoryAdmin from "../pages/admin/InventoryAdmin";
import InventoryDetailAdmin from "../pages/admin/InventoryDetailAdmin";
import InventoryManager from "../pages/manager/InventoryManager";
import InventoryMarket from "../pages/supermarket/InventoryMarket";
import LoginPage from "../pages/login/LoginPage";
import NotFound from "../pages/not_found/NotFound";
import Product from "../pages/admin/Product";
import ProductInStorage from "../pages/admin/ProductInStorage";
import ProductOfManager from "../pages/manager/ProductOfManager";
import ProductOfMarket from "../pages/supermarket/ProductOfMarket";
import RevenueAdmin from "../pages/admin/RevenueAdmin";
import RevenueManager from "../pages/manager/RevenueManager";
import RevenueMarket from "../pages/supermarket/RevenueMarket";
import Shipper from "../pages/admin/Shipper";

function Routers() {
  return (
    <Routes>
      <Route path="/" Component={LoginPage}></Route>
      <Route path="/admin" Component={IndexAdmin}>
        {/* index dashboard */}
        <Route index Component={DashBoardAdmin}></Route>

        {/* Đơn hàng */}
        <Route path="/admin/bills" Component={Bills}></Route>
        <Route path="/admin/bills/detail/:id" Component={BillDetail}></Route>

        {/* product */}
        <Route path="/admin/category" Component={Category}></Route>
        <Route path="/admin/product" Component={Product}></Route>
        <Route path="/admin/product/:id/:storageName" Component={ProductInStorage}></Route>

        {/* shipper */}
        <Route path="/admin/shipper" Component={Shipper}></Route>

        {/* account */}
        <Route path="/admin/account/manager" Component={AccountManager}></Route>
        <Route path="/admin/account/supermarket" Component={AccountMarket}></Route>

        {/* Báo cáo */}
        <Route path="/admin/reports/inventory" Component={InventoryAdmin}></Route>
        <Route path="/admin/reports/inventory/detail/:id/:storageName" Component={InventoryDetailAdmin}></Route>
        <Route path="/admin/reports/inventory/detail/list/:id/in/:storageId/:storageName" Component={DetailList}></Route>
        <Route path="/admin/reports/bill" Component={ChartOverviewBill}></Route>

        <Route path="/admin/reports/revenue" Component={RevenueAdmin}></Route>

        {/* <Route path="/admin/reports/revenue" Component={DoughtnutRevenue}></Route> */}

      </Route>
      <Route path="/manager" Component={IndexManager}>
        {/* dashboard manager */}
        <Route index Component={DashboardManager}></Route>

        {/* phiếu xuất , phiếu nhập */}
        <Route path="/manager/bills/export" Component={ExportBill}></Route>
        <Route path="/manager/bills/import" Component={ImportBill}></Route>
        <Route path="/manager/bills/create" Component={CreateBillManager}></Route>
        <Route path="/manager/bills/detail/:id" Component={BillDetailManager}></Route>
        <Route path="/manager/bills/edit/:id" Component={BillDetailEdit}></Route>

        {/* product */}
        <Route path="/manager/products" Component={ProductOfManager}></Route>

        {/* báo cáo */}
        <Route path="/manager/reports/inventory" Component={InventoryManager}></Route>
        <Route path="/manager/reports/inventory/detail/:id/:created" Component={DetailListManager}></Route>

        <Route path="/manager/reports/bill" Component={ChartExportAndImport}></Route>

        <Route path="/manager/reports/revenue" Component={RevenueManager}></Route>

        {/* information */}
        <Route path="/manager/information" Component={InformationManager}></Route>
      </Route>
      <Route path="/market" Component={IndexSuperMarket}>
        {/* dashboard market */}
        <Route index Component={DashboardMarket}></Route>

        {/* phiếu xuất , phiếu nhập */}
        <Route path="/market/bills/export" Component={ExportBillMarket}></Route>
        <Route path="/market/bills/import" Component={ImportBillMarket}></Route>
        <Route path="/market/bills/create" Component={CreateBillMarket}></Route>
        <Route path="/market/bills/detail/:id" Component={BillDetailMarket}></Route>
        <Route path="/market/bills/edit/:id" Component={BillDetailEditMarket}></Route>

        {/* product */}
        <Route path="/market/products" Component={ProductOfMarket}></Route>

        {/* báo cáo */}
        <Route path="/market/reports/inventory" Component={InventoryMarket}></Route>
        <Route path="/market/reports/inventory/detail/:id/:created" Component={DetailListMarket}></Route>
        <Route path="/market/reports/bill" Component={ChartExportAndImportMarket}></Route>

        <Route path="/market/reports/revenue" Component={RevenueMarket}></Route>

        {/* information */}
        <Route path="/market/information" Component={InformationMarket}></Route>
      </Route>
      <Route path="*" Component={NotFound}></Route>
    </Routes>
  );
}

export default Routers;
