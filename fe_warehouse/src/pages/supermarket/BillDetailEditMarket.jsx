import { Button, FormControl, FormControlLabel, InputLabel, MenuItem, Paper, Radio, RadioGroup, Select, Table, TableBody, TableCell, TableContainer, TableHead, TableRow, TextField } from '@mui/material';
import { DATA_BILL_LOCAL, DATA_CATEGORY, DATA_PRODUCT, DATA_SHIP } from '../../redux/selectors/selectors';
import React, { useEffect, useState } from 'react'
import { addProduct, changeQuantity, minusProduct, plusProduct, removeProduct, resetBillLocal, setEndStorageId, setShipmentId, setStartStorageId } from '../../redux/reducers/billLocalSlice';
import { useDispatch, useSelector } from 'react-redux';
import { useNavigate, useParams } from 'react-router-dom'

import AddCircleOutlineOutlinedIcon from '@mui/icons-material/AddCircleOutlineOutlined';
import ArrowRightIcon from '@mui/icons-material/ArrowRight';
import DeleteIcon from '@mui/icons-material/Delete';
import DescriptionIcon from '@mui/icons-material/Description';
import { GET_ALL_CATEGORY } from '../../redux/api/service/categoryService';
import { GET_ALL_PRODUCT_BY_STORAGE_ID } from '../../redux/api/service/productService';
import { GET_ALL_SHIP } from '../../redux/api/service/shipService';
import InventoryDetail from '../../components/modal/InventoryDetail';
import PersonIcon from '@mui/icons-material/Person';
import { UPDATE_BILL } from '../../redux/api/service/billService';
import WarehouseIcon from '@mui/icons-material/Warehouse';
import instance from '../../redux/api';

function BillDetailEditMarket() {

  const { id } = useParams();
  const productInStorage = useSelector(DATA_PRODUCT);
  const billLocal = useSelector(DATA_BILL_LOCAL);
  const categories = useSelector(DATA_CATEGORY);
  const dispatch = useDispatch();
  const navigate = useNavigate();

  const [user, setUser] = useState(JSON.parse(localStorage.getItem('user')));

  const [reload, setReload] = useState(false);
  const handleReload = () => setReload(!reload);

  const ship = useSelector(DATA_SHIP);
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

  const [data, setData] = useState();

  const handleLoadBill = () => {
    instance.get(`/api/v1/shipping-report/bill/${id}`)
      .then(resp => {
        dispatch(setStartStorageId(resp.data.start.id));
        dispatch(setEndStorageId(resp.data.end.id));
        dispatch(setShipmentId(resp.data.bill.shipment.id));
        resp.data.billDetail.map((item) => (dispatch(addProduct({
          productId: item.product.id,
          id: item.product.id,
          code: item.product.code,
          name: item.product.productName,
          quantity: item.quantity,
          price: item.product.price,
          stock: item.product.quantity,
          total: item.total,
        }))))

        dispatch(GET_ALL_PRODUCT_BY_STORAGE_ID({ search: searchProduct, id: resp.data.start.id }));
        setData(resp.data);
      })
      .catch(err => console.log(err))
  }

  // start handle product
  const handleAddProduct = (item) => {
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
    dispatch(addProduct(result));
    setSearchProduct("")
    setErrorProducts('')
  }

  const totalProducts = () => {
    let sum = 0;
    for (let i = 0; i < billLocal.products.length; i++) {
      sum += billLocal.products[i].price * billLocal.products[i].quantity;
    }
    return sum;
  }

  const handlePlusProduct = (id) => {
    dispatch(plusProduct(id));
  }

  const handleInputChangeQuantity = (e, id) => {
    dispatch(changeQuantity({ quantity: e.target.value, id: id }))
  }

  const handleMinusProduct = (id) => {
    dispatch(minusProduct(id))
  }

  const handleDelete = (id) => {
    dispatch(removeProduct(id));
  };
  // end handle product

  const [errorProducts, setErrorProducts] = useState();

  // start handle uadate bill
  const handleUpdateBill = () => {

    if (billLocal.products.length === 0) {
      setErrorProducts("Vui Lòng Thêm Sản Phẩm");
      return;
    }

    dispatch(UPDATE_BILL({ data: billLocal, id: id }));
    dispatch(resetBillLocal());
    setTimeout(() => {
      navigate("/market/bills/export")
    }, 100)
  }
  // end handle update bill

  // start go back
  const handleGoBack = () => {
    dispatch(resetBillLocal());
    navigate("/market/bills/export");
    handleReload();
  }
  // end go back

  const [searchProduct, setSearchProduct] = useState("");

  // start open inventory
  const [openInventory, setOpenInventory] = useState(false);
  const handleOpenInventory = () => setOpenInventory(true);
  const handleCloseInventory = () => setOpenInventory(false);
  // end open inventory

  // start price ship
  const getPriceForShip = () => {
    for (let i = 0; i < ship.length; i++) {
      if (ship[i].id == shipId) {
        return ship[i].price;
      }
    }
  }
  // end price ship

  const [myStorage, setMyStorage] = useState(null);
  const handleLoadStorageByUserId = async () => {
    await instance.get(`/api/v1/storage/findByUserId/${user.id}`)
      .then((resp) => setMyStorage(resp.data))
      .catch((err) => console.log(err));
  }

  // handle category 
  const [category, setCategory] = useState('ALL');
  const handleChange = (event) => (setCategory(event.target.value));

  useEffect(() => {
    handleLoadStorageByUserId();
    handleLoadBill();
  }, [])

  useEffect(() => {
    if (myStorage) {
      dispatch(setStartStorageId(myStorage.id));
    } else {

    }
    if (user) {
      dispatch(GET_ALL_PRODUCT_BY_STORAGE_ID({ search: searchProduct, filter: category, id: user.storageId }));
    } else {
      navigate("/")
    }
    dispatch(GET_ALL_SHIP(''));
    dispatch(GET_ALL_CATEGORY(''));
  }, [dispatch, searchProduct, reload])

  return (
    <div className='mx-36'>
      <div className="service flex gap-6">
        <div className='flex-1 bg-white p-5 rounded-md'>
          <h2 className="text-xl text-center">Nơi Gửi Hàng</h2>
          <div className='p-3'>
            {data &&
              <div>
                <div className="info_user border-b-2 border-black pb-2">
                  <h2 className='text-lg font-semibold flex items-center justify-between'>
                    <span><PersonIcon className='mr-1' /> Thông tin người gửi: </span><p></p>
                  </h2>
                  <p className='text-lg font-semibold text-blue-600 '>
                    {
                      data.start.users.sex ? <>Anh {data.start.users.firstName + " " + data.start.users.lastName}</> :
                        <>Chị {data.start.users.firstName + " " + data.start.users.lastName}</>
                    }<span className='text-black'>{" - " + data.start.users.phone + " "} </span>
                  </p>
                </div>
                <div className="info_storae">
                  <h2 className='text-lg font-semibold mt-2 flex items-center'>
                    <WarehouseIcon className='mr-2' />{data.start.typeStorage === "STORAGE" ? 'Thông tin kho hàng:' : 'Thông tin siêu thị:'}
                  </h2>
                  <ul className='mt-3'>
                    <li><ArrowRightIcon /> Số Điện Thoại : {data.start.users.phone}</li>
                    <li><ArrowRightIcon /> Email : {data.start.users.email}</li>
                    <li><ArrowRightIcon /> Địa Chỉ : {data.start.address.toUpperCase()}</li>
                    <li><ArrowRightIcon /> Khu Vực : {data.start.zone.zoneName}</li>
                  </ul>
                </div>
              </div>
            }
          </div>
        </div>
        <div className='flex-1 bg-white p-5 rounded-md'>
          <h2 className='text-xl text-center'>Nơi Nhận Hàng</h2>
          <div className='p-3'>
            {data &&
              <div>
                <div className="info_user border-b-2 border-black pb-2">
                  <h2 className='text-lg font-semibold flex items-center justify-between'>
                    <span><PersonIcon className='mr-1' /> Thông tin người nhận: </span><p></p>
                  </h2>
                  <p className='text-lg font-semibold text-blue-600 '>
                    {
                      data.end.users.sex ? <>Anh {data.end.users.firstName + " " + data.end.users.lastName}</> :
                        <>Chị {data.end.users.firstName + " " + data.end.users.lastName}</>
                    }<span className='text-black'>{" - " + data.end.users.phone + " "} </span>
                  </p>
                </div>
                <div className="info_storae">
                  <h2 className='text-lg font-semibold mt-2 flex items-center'>
                    <WarehouseIcon className='mr-2' />{data.end.typeStorage === "STORAGE" ? 'Thông tin kho hàng:' : 'Thông tin siêu thị:'}
                  </h2>
                  <ul className='mt-3'>
                    <li><ArrowRightIcon /> Số Điện Thoại : {data.end.users.phone}</li>
                    <li><ArrowRightIcon /> Email : {data.end.users.email}</li>
                    <li><ArrowRightIcon /> Địa Chỉ : {data.end.address.toUpperCase()}</li>
                    <li><ArrowRightIcon /> Khu Vực : {data.end.zone.zoneName}</li>
                  </ul>
                </div>
              </div>
            }
          </div>
        </div>
        <div className='flex-1 bg-white p-5 rounded-md'>
          <h2 className='text-xl text-center'>Đơn Vị Giao Hàng</h2>
          <div className='flex py-3'>
            <FormControl fullWidth>
              <RadioGroup
                aria-labelledby="demo-radio-buttons-group-label"
                defaultValue="female"
                name="radio-buttons-group"
              >
                {ship.map((item) => {
                  if (item.status) {
                    if (shipId) {
                      return shipId == item.id ? (
                        <FormControlLabel
                          sx={styleActive}
                          value={item.id}
                          control={<Radio checked />}
                          onChange={handleChangeShipId}
                          label={<p><span>{item.shipName + " "}</span><span>{" " + item.price + "đ / kg"}</span></p>}
                        />
                      ) : (
                        <FormControlLabel
                          sx={styleNonActive}
                          value={item.id}
                          control={<Radio />}
                          onChange={handleChangeShipId}
                          label={<p><span>{item.shipName + " "}</span><span>{" " + item.price + "đ / kg"}</span></p>}
                        />
                      );
                    } else {
                      return data?.bill.shipment.id == item.id ? (
                        <FormControlLabel
                          sx={styleActive}
                          value={item.id}
                          control={<Radio checked />}
                          onChange={handleChangeShipId}
                          label={<p><span>{item.shipName + " "}</span><span>{" " + item.price + "đ / kg"}</span></p>}
                        />
                      ) : (
                        <FormControlLabel
                          sx={styleNonActive}
                          value={item.id}
                          control={<Radio />}
                          onChange={handleChangeShipId}
                          label={<p><span>{item.shipName + " "}</span><span>{" " + item.price + "đ / kg"}</span></p>}
                        />
                      );
                    }
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
                        return (item.statusName !== "PENDING" && item.statusName !== "DELETE" ? <li onClick={() => handleAddProduct(item)} key={item.id} className='border-b-2 border-t-2 border-slate-200 p-2 flex items-center hover:bg-cyan-50 transition-all hover:cursor-pointer'>
                          <div className='flex-1'>
                            <p className='flex justify-between text-lg font-semibold'><span>{item.productName}</span><span>{item.price.toLocaleString()} VND</span></p>
                            <p className='text-right text-red-600'>Đã hết hàng</p>
                          </div>
                        </li> : '')
                      } else {
                        return (item.statusName !== "PENDING" && item.statusName !== "DELETE" ? <li onClick={() => handleAddProduct(item)} key={item.id} className='border-b-2 border-t-2 border-slate-200 p-2 flex items-center hover:bg-cyan-50 transition-all hover:cursor-pointer'>
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
      {/* nơi hiển thị chi tiết những sản phẩm trong đơn hàng */}
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
              {billLocal.products.length > 0 ? billLocal.products.map((item, index) => {
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
                    <TableCell align='center'>{item.price.toLocaleString()}</TableCell>
                    <TableCell align='center'>{item.total.toLocaleString()}</TableCell>
                    <TableCell align='center'><Button variant="contained" onClick={() => handleDelete(item.id)} color='error'><DeleteIcon /></Button></TableCell>
                  </TableRow>
                )
              }) : (
                <TableRow>
                  <TableCell align='center' className='hover:cursor-pointer' onClick={() => setSearchProduct(" ")}><AddCircleOutlineOutlinedIcon /></TableCell>
                  <TableCell colSpan={6} className='hover:cursor-pointer' onClick={() => setSearchProduct(" ")} align='center'></TableCell>
                </TableRow>
              )}
            </TableBody>
          </Table>
        </TableContainer>
      </div>
      {/* nơi thông tin tổng tiền */}
      <div className="total_detail bg-white flex justify-end p-5">
        <div className='w-96'>
          <ul className='flex flex-col gap-2'>
            <li className='flex justify-between font-normal'><span>Tổng sản phẩm : </span><span>{data?.billDetail.reduce((sum, item) => { return sum += item.quantity }, 0)}</span></li>
            <li className='flex justify-between font-normal'><span>Tổng giá tiền : </span><span>{data?.bill.total.toLocaleString()}</span></li>
            <li className='flex justify-between font-normal'><span>Phí ship hàng : </span><span>{shipId ? (data.billDetail.reduce((sum, item) => { return sum += item.product.weight * item.quantity }, 0) * getPriceForShip()).toLocaleString() : data?.bill.shipment.price.toLocaleString()} VND / kg</span></li>
          </ul>
        </div>
      </div>
      <div className="action flex justify-end py-3 gap-3">
        <Button variant="outlined" onClick={handleGoBack}>Quay lại</Button>
        <Button variant="contained" onClick={handleUpdateBill} >Cập Nhật</Button>
      </div>
    </div >
  )
}

export default BillDetailEditMarket