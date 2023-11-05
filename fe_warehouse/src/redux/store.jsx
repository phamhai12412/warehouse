import authSlice from "./reducers/authSlice";
import billExportSlice from "./reducers/billExportSlice";
import billImportSlice from "./reducers/billImportSlice";
import billLocalSlice from "./reducers/billLocalSlice";
import billRemoteSlice from "./reducers/billRemoteSlice";
import categorySlice from "./reducers/categorySlice";
import { configureStore } from "@reduxjs/toolkit";
import inventorySlice from "./reducers/inventorySlice";
import productSlice from "./reducers/productSlice";
import shipSlice from "./reducers/shipSlice";
import storageSlice from "./reducers/storageSlice";
import userSlice from "./reducers/userSlice";
import zoneSlice from "./reducers/zoneSlice";

export default configureStore({
  reducer: {
    auth: authSlice,
    user: userSlice,
    zone: zoneSlice,
    billLocal: billLocalSlice,
    billRemote: billRemoteSlice,
    storage: storageSlice,
    category: categorySlice,
    ship: shipSlice,
    product: productSlice,
    billExport: billExportSlice,
    billImport: billImportSlice,
    inventory: inventorySlice,
  },
});
