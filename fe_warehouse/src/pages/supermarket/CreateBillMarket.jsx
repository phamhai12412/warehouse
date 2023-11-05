import { Button, FormControl, FormControlLabel, InputLabel, MenuItem, Paper, Radio, RadioGroup, Select, Table, TableBody, TableCell, TableContainer, TableHead, TableRow, TextField, Tooltip } from '@mui/material'
import { DATA_BILL_LOCAL, DATA_CATEGORY, DATA_PRODUCT, DATA_SHIP, DATA_STORAGE } from '../../redux/selectors/selectors';
import React, { useEffect, useState } from 'react'
import { addProduct, changeQuantity, minusProduct, plusProduct, removeEndStorageId, removeProduct, resetBillLocal, setBillLocal, setEndStorageId, setShipmentId, setStartStorageId } from '../../redux/reducers/billLocalSlice';
import toast, { Toaster } from 'react-hot-toast';
import { useDispatch, useSelector } from 'react-redux';

import { ADD_BILL } from '../../redux/api/service/billService';
import AddCircleOutlineOutlinedIcon from '@mui/icons-material/AddCircleOutlineOutlined';
import AddIcon from '@mui/icons-material/Add';
import ArrowRightIcon from '@mui/icons-material/ArrowRight';
import CloseIcon from '@mui/icons-material/Close';
import DeleteIcon from '@mui/icons-material/Delete';
import DescriptionIcon from '@mui/icons-material/Description';
import { GET_ALL_CATEGORY } from '../../redux/api/service/categoryService';
import { GET_ALL_PRODUCT_BY_STORAGE_ID } from '../../redux/api/service/productService';
import { GET_ALL_SHIP } from '../../redux/api/service/shipService';
import { GET_ALL_STORAGE } from '../../redux/api/service/storageService';
import HelpRecipient from '../../components/help/HelpRecipient';
import InventoryDetail from '../../components/modal/InventoryDetail';
import PersonIcon from '@mui/icons-material/Person';
import QuestionMarkIcon from '@mui/icons-material/QuestionMark';
import WarehouseIcon from '@mui/icons-material/Warehouse';
import instance from '../../redux/api';
import { useNavigate } from 'react-router-dom';

function CreateBillMarket() {

  const navigate = useNavigate();
  const productInStorage = useSelector(DATA_PRODUCT);
  const categories = useSelector(DATA_CATEGORY);
  const bill = useSelector(DATA_BILL_LOCAL);
  const storage = useSelector(DATA_STORAGE);
  const ship = useSelector(DATA_SHIP);
  const dispatch = useDispatch();

  const [shipId, setShipId] = useState(null);
  const handleChangeShipId = (e) => {
    setShipId(e.target.value);
    dispatch(setShipmentId(e.target.value));
  };

  const styleActive = {
    border: '3px solid red',
    padding: '5px 10px',
    minWidth: '150px',
    borderRadius: '8px',
    margin: '5px',
    transition: '0.3s',
  };
  const styleNonActive = {
    border: '3px solid #000',
    padding: '5px 10px',
    minWidth: '150px',
    borderRadius: '8px',
    margin: '5px',
    transition: '0.3s',
  };
  const styleNonActiveError = {
    border: '3px solid orange',
    padding: '5px 10px',
    minWidth: '150px',
    borderRadius: '8px',
    margin: '5px',
    transition: '0.3s',
  };

  const [searchRecipients, setSearchRecipients] = useState("");

  const handleSearchUserRecipients = (e) => { setSearchRecipients(e.target.value) }

  const [userRecipient, setUserRecipient] = useState(null);

  const handleCloseUserRecipient = () => {
    dispatch(removeEndStorageId(userRecipient.id));
    setUserRecipient(null);
    setSearchRecipients("");
  }

  const [openInventory, setOpenInventory] = useState(false);
  const handleOpenInventory = () => setOpenInventory(true);
  const handleCloseInventory = () => setOpenInventory(false);

  const [searchProduct, setSearchProduct] = useState("");

  const handleAddProductToBill = (item) => {
    if (item.quantity > 0) {
      const result = {
        productId: item.id,
        id: item.id,
        code: item.code,
        name: item.productName,
        quantity: 1,
        price: item.price,
        stock: item.quantity,
        weight: item.weight,
        total: item.price,
      }
      setSearchProduct("")
      dispatch(addProduct(result));
    } else {
      toast.error("Không đủ số lượng", { position: 'bottom-right' })
    }
    setErrorProducts("")
  }

  const handlePlusProduct = (id) => {
    dispatch(plusProduct(id));
  }

  const handleInputChangeQuantity = (e, id) => {
    dispatch(changeQuantity({ quantity: e.target.value, id: id }))
  }

  const handleMinusProduct = (id) => {
    dispatch(minusProduct(id));
  }

  const handleDeleteProductInBill = (id) => {
    dispatch(removeProduct(id))
  }

  const totalProducts = () => {
    let sum = 0;
    for (let i = 0; i < bill.products.length; i++) {
      sum += bill.products[i].price * bill.products[i].quantity;
    }
    return sum;
  }

  const handleGoBack = () => {
    setSearchRecipients("");
    setUserRecipient(null);
    dispatch(resetBillLocal());
    navigate("/market/bills/export");
  }

  const [errorEndStorage, setErrorEndStorage] = useState();
  const [errorShipment, setErrorShipmemt] = useState();
  const [errorProducts, setErrorProducts] = useState();

  const handleAddBill = () => {
    if (bill) {
      if (!bill.endStorageId) {
        setErrorEndStorage("Vui Lòng Chọn Nơi Nhận Hàng")
        return;
      }
      if (!bill.shipmentId) {
        setErrorShipmemt("Vui Lòng Chọn đơn vị vận chuyển")
        return;
      }
      if (bill.products.length === 0) {
        setErrorProducts("Vui Lòng Thêm Sản Phẩm")
        return;
      }
      dispatch(ADD_BILL(bill));
      dispatch(resetBillLocal());
      setSearchRecipients("");
      setUserRecipient(null);
      setTimeout(() => {
        navigate("/market/bills/export");
      }, 100)
    }
  }

  const getPriceForShip = () => {
    for (let i = 0; i < ship.length; i++) {
      if (ship[i].id == shipId) {
        return ship[i].price;
      }
    }
  }

  const [category, setCategory] = useState('ALL');
  const handleChange = (event) => (setCategory(event.target.value));


  const [myStorage, setMyStorage] = useState(null);
  const [user, setUser] = useState(JSON.parse(localStorage.getItem("user")));

  const handleLoadStorageByUserId = async () => {
    await instance.get(`/api/v1/storage/findByUserId/${user.id}`)
      .then((resp) => setMyStorage(resp.data))
      .catch((err) => console.log(err));
  }

  useEffect(() => {
    if (user) {
      handleLoadStorageByUserId();
      dispatch(setBillLocal({
        startStorageId: user.storageId,
        endStorageId: '',
        shipmentId: '',
        products: []
      }));
    } else {
      navigate('/')
    }
    // xóa state trước khi vào đây

    setErrorEndStorage("");
    setErrorShipmemt("");
    setErrorProducts("");
  }, [])

  useEffect(() => {
    if (myStorage) {
      dispatch(setStartStorageId(myStorage.id));
    }
    if (user) {
      dispatch(GET_ALL_PRODUCT_BY_STORAGE_ID({ search: searchProduct, filter: category, id: user.storageId }));
    } else {
      navigate('/')
    }
    if (searchRecipients) {
      dispatch(GET_ALL_STORAGE(searchRecipients));
    }
    dispatch(GET_ALL_CATEGORY(''));
    dispatch(GET_ALL_SHIP(''));
  }, [dispatch, searchRecipients, searchProduct])

  return (
    <div className='mx-36'>
      <div className="service flex gap-6">
        <div className='flex-1 bg-white p-5 rounded-md'>
          <h2 className="text-xl text-center">Nơi Gửi Hàng</h2>
          <div className='p-3'>
            {myStorage && <div>
              <div className="info_user border-b-2 border-black pb-2">
                <h2 className='text-lg font-semibold flex items-center justify-between'>
                  <span><PersonIcon className='mr-1' /> Thông tin người gửi: </span>
                </h2>
                <p className='text-lg font-semibold text-blue-600 '>
                  {
                    user.sex ? <>Anh {user.firstName + " " + user.lastName}</> :
                      <>Chị {user.firstName + " " + user.lastName}</>
                  }<span className='text-black'>{" - " + myStorage.users.phone + " "} </span>
                </p>
              </div>
              <div className="info_storae">
                <h2 className='text-lg font-semibold mt-2 flex items-center'>
                  <WarehouseIcon className='mr-2' />{myStorage.typeStorage === "STORAGE" ? 'Thông tin kho hàng:' : 'Thông tin siêu thị:'}
                </h2>
                <ul className='mt-3'>
                  <li><ArrowRightIcon /> <b>Số Điện Thoại : </b> {myStorage.users.phone}</li>
                  <li><ArrowRightIcon /> <b>Email :</b>{myStorage.users.email}</li>
                  <li><ArrowRightIcon /> <b>Địa Chỉ :</b>{myStorage.address.toUpperCase()}</li>
                  <li><ArrowRightIcon /> <b>Khu Vực :</b>{myStorage.zone.zoneName}</li>
                </ul>
              </div>
            </div>}
          </div>
        </div>
        <div className='flex-1 bg-white p-5 rounded-md'>
          <h2 className='text-xl text-center'>Nơi Nhận Hàng</h2>
          <div className='p-3'>
            {userRecipient ?
              (<div>
                <div className="info_user border-b-2 border-black pb-2">
                  <h2 className='text-lg font-semibold flex items-center justify-between'>
                    <span><PersonIcon className='mr-1' /> Thông tin người nhận: </span><CloseIcon className='hover:cursor-pointer' onClick={handleCloseUserRecipient} />
                  </h2>
                  <p className='text-lg font-semibold text-blue-600 '>
                    {
                      userRecipient.users.sex ? <>Anh {userRecipient.users.firstName + " " + userRecipient.users.lastName}</> :
                        <>Chị {userRecipient.users.firstName + " " + userRecipient.users.lastName}</>
                    }<span className='text-black'>{" - " + userRecipient.users.phone + " "} </span>
                  </p>
                </div>
                <div className="info_storae">
                  <h2 className='text-lg font-semibold mt-2 flex items-center'>
                    <WarehouseIcon className='mr-2' />{userRecipient.typeStorage === "STORAGE" ? 'Thông tin kho hàng:' : 'Thông tin siêu thị:'}
                  </h2>
                  <ul className='mt-3'>
                    <li><ArrowRightIcon /> <b>Số Điện Thoại :</b>  {userRecipient.users.phone}</li>
                    <li><ArrowRightIcon /> <b>Email :</b>  {userRecipient.users.email}</li>
                    <li><ArrowRightIcon /> <b>Địa Chỉ :</b>  {userRecipient.address.toUpperCase()}</li>
                    <li><ArrowRightIcon /> <b>Khu Vực :</b>  {userRecipient.zone.zoneName}</li>
                  </ul>
                </div>
              </div>) :
              (<div className="search relative">
                <TextField
                  error={errorEndStorage}
                  className='relative' id="outlined-basic" size='small'
                  onChange={handleSearchUserRecipients}
                  value={searchRecipients}
                  fullWidth label={errorEndStorage ? errorEndStorage : "Nơi nhận hàng"}
                  variant="outlined"
                />
                {searchRecipients &&
                  <div className='absolute w-full mt-2 bg-slate-100 p-1 shadow-md'>
                    {
                      storage && storage.map((item) => {
                        return (item.id !== myStorage.id && item.typeStorage !== "STORAGE" ? <p key={item.id} onClick={() => { setUserRecipient(item); dispatch(setEndStorageId(item.id)) }} className='p-2 m-2 bg-white shadow-md hover:cursor-pointer'>{item.storageName}</p> : '')
                      })
                    }
                  </div>
                }
                <Tooltip title={<HelpRecipient />}><QuestionMarkIcon className='absolute right-3 top-1/2 -translate-y-1/2 hover:cursor-pointer' /></Tooltip>
              </div>)}
          </div>
        </div>
        <div className='flex-1 bg-white p-5 rounded-md'>
          <h2 className='text-xl text-center'>Đơn Vị Giao Hàng</h2>
          <p className='text-center text-red-600'>{errorShipment ? errorShipment : ''}</p>
          <div className='flex justify-center p-5'>
            <FormControl fullWidth>
              <RadioGroup
                aria-labelledby="demo-radio-buttons-group-label"
                defaultValue="female"
                name="radio-buttons-group"
              >
                {ship.map((item) => {
                  if (item.status) {
                    return shipId == item.id ? (
                      <Tooltip title={item.price.toLocaleString() + " VND / kg"}>
                        <FormControlLabel
                          sx={styleActive}
                          value={item.id}
                          control={<Radio />}
                          onChange={handleChangeShipId}
                          label={<span>{item.shipName}</span>}
                        />
                      </Tooltip>
                    ) : (
                      <Tooltip title={item.price.toLocaleString() + " VND / kg"}>
                        <FormControlLabel
                          sx={errorShipment ? styleNonActiveError : styleNonActive}
                          value={item.id}
                          control={<Radio />}
                          onChange={handleChangeShipId}
                          label={<span>{item.shipName}</span>}
                        />
                      </Tooltip>
                    );
                  }
                })}
              </RadioGroup>
            </FormControl>
          </div>

        </div>
      </div>
      <div className="bill bg-white mt-6 p-5">
        <h2 className='text-lg font-semibold flex items-center gap-2'><DescriptionIcon /> <span>Thông tin chi tiết đơn hàng</span></h2>
        <div className="search_product flex gap-3 mt-3">
          <div className='flex-1 flex gap-2'>
            <FormControl sx={{ width: "20%" }} size='small'>
              <InputLabel id="demo-simple-select-label">Lọc</InputLabel>
              <Select
                labelId="demo-simple-select-label"
                id="demo-simple-select"
                value={category}
                label="Filter"
                onChange={handleChange}
              >
                <MenuItem value={'ALL'}>TẤT CẢ</MenuItem>
                {categories && categories.map((item) => (<MenuItem value={item.categoryName}>{item.categoryName.toUpperCase()}</MenuItem>))}
              </Select>
            </FormControl>
            <div className='relative flex-1'>
              <TextField
                error={errorProducts}
                fullWidth id="outlined-basic"
                value={searchProduct}
                onChange={(e) => setSearchProduct(e.target.value)}
                size='small' label={errorProducts ? errorProducts : "Tìm kiếm theo tên hoặc mã sản phẩm"}
                variant="outlined"
              />
              {searchProduct &&
                (
                  <ul className='absolute bg-white w-full mt-2 shadow-2xl p-5'>
                    {productInStorage && productInStorage.map((item) => {
                      if (item.quantity === 0) {
                        return (item.statusName !== "PENDING" && item.statusName !== "DELETE" ? <li onClick={() => handleAddProductToBill(item)} key={item.id} className='border-b-2 border-t-2 border-slate-200 p-2 flex items-center hover:bg-cyan-50 transition-all hover:cursor-pointer'>
                          <div className='flex-1'>
                            <p className='flex justify-between text-lg font-semibold'><span>{item.productName}</span><span>{item.price.toLocaleString()} VND</span></p>
                            <p className='text-right text-red-600'>Đã hết hàng</p>
                          </div>
                        </li> : '')
                      } else {
                        return (item.statusName !== "PENDING" && item.statusName !== "DELETE" ? <li onClick={() => handleAddProductToBill(item)} key={item.id} className='border-b-2 border-t-2 border-slate-200 p-2 flex items-center hover:bg-cyan-50 transition-all hover:cursor-pointer'>
                          <div className='flex-1'>
                            <p className='flex justify-between text-lg font-semibold'><span className='text-blue-600'>{item.productName}</span><span>{item.price.toLocaleString()} VND</span></p>
                            <p className='flex justify-between text-lg font-semibold'><span><b>Mã sản phẩm: </b><span className='text-blue-600'>{item.code}</span></span><span>Tồn : {item.quantity} | Có thể Bán : {item.quantity}</span></p>
                          </div>
                        </li> : '')
                      }
                    })}
                  </ul>
                )
              }
            </div>
          </div>
          <Button onClick={handleOpenInventory} variant="contained">Kiểm Tra Tồn Kho</Button>
          {myStorage && <InventoryDetail
            openInventory={openInventory}
            handleCloseInventory={handleCloseInventory}
            userSend={myStorage}
          />}
        </div>
      </div>
      <div className="bill_detail bg-white border-t-2 border-slate-300">
        <TableContainer component={Paper} >
          <Table sx={{ width: '100%' }} aria-label="simple table">
            <TableHead sx={{ bgcolor: '#ecf0f1' }}>
              <TableRow>
                <TableCell align='center'>STT</TableCell>
                <TableCell align='center'>MÃ SẢN PHẨM</TableCell>
                <TableCell align='left'>TÊN</TableCell>
                <TableCell align='center'>SỐ LƯỢNG</TableCell>
                <TableCell align='center'>GIÁ THÀNH</TableCell>
                <TableCell align='center'>TỔNG TIỀN</TableCell>
                <TableCell align='center'>HÀNH ĐỘNG</TableCell>
              </TableRow>
            </TableHead>
            <TableBody>
              {
                bill.products.length > 0 ? bill.products.map((item, index) => {
                  return (
                    <TableRow key={item.id}>
                      <TableCell align='center'>{index + 1}</TableCell>
                      <TableCell align='center'>{item.code}</TableCell>
                      <TableCell align='left'>{item.name}</TableCell>
                      <TableCell align='center'>
                        <div className="quantity flex justify-center items-center gap-4">
                          <p onClick={() => handleMinusProduct(item.id)} className='bg-slate-200 p-2 rounded-full flex items-center justify-center w-7 h-7 hover:cursor-pointer'><i className="fa-solid fa-minus"></i></p>
                          <p>
                            <input type="text" value={item.quantity} onClick={(e) => e.target.select()} onChange={(e) => handleInputChangeQuantity(e, item.id)} style={{ width: '50px', textAlign: 'center', outline: 'none', borderBottom: '2px solid blue' }} />
                          </p>
                          <p onClick={() => handlePlusProduct(item.id)} className='bg-slate-200 p-2 rounded-full flex items-center justify-center w-7 h-7 hover:cursor-pointer'><i className="fa-solid fa-plus"></i></p>
                        </div>
                      </TableCell>
                      <TableCell align='center'>{item.price.toLocaleString() + " đ"}</TableCell>
                      <TableCell align='center'>{item.total.toLocaleString() + " đ"}</TableCell>
                      <TableCell align='center'><Button variant="contained" color='error' onClick={() => handleDeleteProductInBill(item.id)}><DeleteIcon /></Button></TableCell>
                    </TableRow>
                  )
                }) : (
                  <TableRow>
                    <TableCell align='center' className='hover:cursor-pointer' onClick={() => setSearchProduct(" ")}><AddCircleOutlineOutlinedIcon /></TableCell>
                    <TableCell colSpan={6} className='hover:cursor-pointer' onClick={() => setSearchProduct(" ")} align='center'></TableCell>
                  </TableRow>
                )
              }
            </TableBody>
          </Table>
        </TableContainer>
      </div>
      {/* nơi thông tin tổng tiền */}
      <div className="total_detail bg-white flex justify-end p-5">
        <div className='w-96'>
          <ul className='flex flex-col gap-2'>
            <li className='flex justify-between font-normal'><span>Tổng sản phẩm : </span><span>{bill.products.reduce((sum, item) => { return sum += item.quantity }, 0)}</span></li>
            <li className='flex justify-between font-normal'><span>Tổng giá tiền : </span><span>{totalProducts(bill.products).toLocaleString() + " đ"}</span></li>
            <li className='flex justify-between font-normal'><span>Phí ship hàng : </span><span>{shipId ? (bill.products.reduce((sum, item) => { return sum += item.weight * item.quantity }, 0) * getPriceForShip()).toLocaleString() : '0'} VND / kg</span></li>
          </ul>
        </div>
      </div>
      <div className="action flex justify-end py-3 gap-3">
        <Button variant="outlined" onClick={handleGoBack}>Quay lại</Button>
        <Button variant="contained" onClick={handleAddBill}><AddIcon /> Tạo đơn hàng</Button>
      </div>
      <Toaster />
    </div >
  )
}

export default CreateBillMarket