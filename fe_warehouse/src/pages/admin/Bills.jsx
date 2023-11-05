import { Button, Chip, CircularProgress, FormControl, InputLabel, MenuItem, Paper, Select, Table, TableBody, TableCell, TableContainer, TableHead, TableRow, TextField, Tooltip } from '@mui/material'
import { DELIVERY_BILL, GET_ALL_BILL, REJECT_BILL, SUCCESS_BILL } from '../../redux/api/service/billService';
import React, { useEffect, useState } from 'react'
import { TIME_OUT, debouncing } from '../../utils/Deboucing';
import { useDispatch, useSelector } from 'react-redux';

import BillConfirm from '../../components/modal/BillConfirm';
import CancelIcon from '@mui/icons-material/Cancel';
import CheckCircleIcon from '@mui/icons-material/CheckCircle';
import CheckIcon from '@mui/icons-material/Check';
import { DATA_BILL_REMOTE } from '../../redux/selectors/selectors';
import DeleteIcon from '@mui/icons-material/Delete';
import LoadingComponent from '../../components/loading/LoadingComponent';
import LocalShippingIcon from '@mui/icons-material/LocalShipping';
import PrecisionManufacturingIcon from '@mui/icons-material/PrecisionManufacturing';
import { Toaster } from 'react-hot-toast';
import { useNavigate } from 'react-router-dom';

function Bills() {

  const [loading, setLoading] = useState(true);

  const [reload, setReload] = useState(false);
  const handleReload = () => setReload(!reload);

  const bills = useSelector(DATA_BILL_REMOTE);
  const dispatch = useDispatch();
  const navigate = useNavigate();


  const [detail, setDetail] = useState(null);

  const [openConfirm, setOpenConfirm] = useState(false);
  const handleOpenConfirm = () => setOpenConfirm(true);
  const handleCloseConfirm = () => setOpenConfirm(false);
  // start handle status bill
  const handlePrepare = (element) => {
    setDetail(element);
    handleOpenConfirm();
  }

  const handleDelivery = (id) => {
    dispatch(DELIVERY_BILL(id));
    setTimeout(() => {
      handleReload();
    }, 200)
  }

  const handleSuccess = (id) => {
    dispatch(SUCCESS_BILL(id));
    setTimeout(() => {
      handleReload();
    }, 200)
  }

  const handleDelete = (id) => {
    dispatch(REJECT_BILL(id));
    setTimeout(() => {
      handleReload();
    }, 200)
  }
  // end handle status bill

  // handle filter bill
  const [filter, setFilter] = React.useState('ALL');

  const handleChange = (event) => (setFilter(event.target.value));

  const [search, setSearch] = useState("");
  const handleChangeSearch = (e) => setSearch(e.target.value);

  useEffect(() => {
    dispatch(GET_ALL_BILL({ filter: filter, search: search }));
    if (bills) {
      setTimeout(() => {
        setLoading(false);
      }, 800)
    }
  }, [filter, search, reload])

  return (
    <div>
      <div className='flex justify-center text-3xl font-semibold uppercase'>
        quản lý đơn hàng
      </div>
      <div className="content w-full mt-5">
        <div className="header bg-white p-6 shadow-md flex gap-3">
          <FormControl sx={{ width: "20%" }} size='small'>
            <InputLabel id="demo-simple-select-label">Lọc</InputLabel>
            <Select
              labelId="demo-simple-select-label"
              id="demo-simple-select"
              value={filter}
              label="Filter"
              onChange={handleChange}
            >
              <MenuItem value={'ALL'}>TẤT CẢ</MenuItem>
              <MenuItem value={'PENDING'}>CHỜ XÁC NHẬN</MenuItem>
              <MenuItem value={'PREPARE'}>CHUẨN BỊ HÀNG</MenuItem>
              <MenuItem value={'DELIVERY'}>ĐANG GIAO HÀNG</MenuItem>
              <MenuItem value={'SUCCESS'}>THÀNH CÔNG</MenuItem>
              <MenuItem value={'CANCEL'}>HỦY HÀNG</MenuItem>
            </Select>
          </FormControl>
          <TextField id="outlined-basic" size='small' onChange={debouncing(handleChangeSearch, TIME_OUT)} fullWidth label="Tìm kiếm theo điểm bắt đầu" name='search' variant="outlined" />
        </div>
        <div className="table w-full mt-5 rounded-md">
          <TableContainer component={Paper} >
            <Table sx={{ width: '100%' }} aria-label="simple table">
              <TableHead>
                <TableRow className='bg-slate-300'>
                  <TableCell align="center">STT</TableCell>
                  <TableCell align='center'>NGÀY TẠO</TableCell>
                  <TableCell align="center">ĐIỂM BẮT ĐẦU</TableCell>
                  <TableCell align='center'>DIỂM KẾT THÚC</TableCell>
                  <TableCell align='center'>ĐƠN VỊ VẬN CHUYỂN</TableCell>
                  <TableCell align='center'>TỔNG TIỀN</TableCell>
                  <TableCell align='center'>TRẠNG THÁI</TableCell>
                  <TableCell align='center'>HÀNH ĐỘNG</TableCell>
                </TableRow>
              </TableHead>
              <TableBody>
                {!loading && bills && bills.map((e, index) => {
                  return (
                    <TableRow
                      key={e?.bill.id}
                      sx={{ '&:last-child td, &:last-child th': { border: 0 } }}
                      className='hover:bg-slate-100 transition-all hover:cursor-pointer'
                    >
                      <TableCell onClick={() => navigate(`/admin/bills/detail/${e?.bill.id}`)} align='center'>{index + 1}</TableCell>
                      <TableCell onClick={() => navigate(`/admin/bills/detail/${e?.bill.id}`)} align='center'>{e?.bill.created} </TableCell>
                      <TableCell onClick={() => navigate(`/admin/bills/detail/${e?.bill.id}`)} align='center'>{e?.bill.locationStart.toUpperCase()} </TableCell>
                      <TableCell onClick={() => navigate(`/admin/bills/detail/${e?.bill.id}`)} align='center'>{e?.bill.locationEnd.toUpperCase()}</TableCell>
                      <TableCell onClick={() => navigate(`/admin/bills/detail/${e?.bill.id}`)} align='center'>{e?.bill.shipment.shipName}</TableCell>
                      <TableCell onClick={() => navigate(`/admin/bills/detail/${e?.bill.id}`)} align='center'>{(e?.bill.total + e?.bill.priceShip).toLocaleString()}</TableCell>
                      <TableCell onClick={() => navigate(`/admin/bills/detail/${e?.bill.id}`)} align='center'>
                        {e?.delivery === "PENDING" ? <Chip label="CHỜ XÁC NHẬN" color="warning" /> : ''}
                        {e?.delivery === "PREPARE" ? <Chip label="CHUẨN BỊ HÀNG" color="secondary" /> : ''}
                        {e?.delivery === "DELIVERY" ? <Chip label="ĐANG GIAO HÀNG" color="primary" /> : ''}
                        {e?.delivery === "SUCCESS" ? <Chip label="THÀNH CÔNG" color="success" /> : ''}
                        {e?.delivery === "CANCEL" ? <Chip label="HỦY HÀNG" color="error" /> : ''}
                      </TableCell>
                      <TableCell align='center'>
                        {e?.delivery === "PENDING" ? <div className='flex gap-2 justify-center'>
                          <Button variant="contained" onClick={() => handlePrepare(e)} color='warning'><Tooltip title="Chấp Thuận"><CheckIcon /></Tooltip></Button>
                          <Button variant="contained" onClick={() => handleDelete(e?.bill.id)} color='error'><Tooltip title='Từ Chối'><DeleteIcon /></Tooltip></Button>
                        </div> : ''}
                        {e?.delivery === "PREPARE" ?
                          <div className='flex gap-2 justify-center'>
                            <Button color='secondary' onClick={() => handleDelivery(e?.bill.id)} variant="contained">
                              <Tooltip title='Chuẩn Bị Hàng'><PrecisionManufacturingIcon /></Tooltip>
                            </Button>
                            <Button color='error' onClick={() => handleDelete(e?.bill.id)} variant="contained">
                              <Tooltip title='Hủy Hàng'><DeleteIcon /></Tooltip>
                            </Button>
                          </div>
                          : ''}
                        {e?.delivery === "DELIVERY" ? <Button onClick={() => handleSuccess(e?.bill.id)} color='primary' variant="contained">
                          <Tooltip title='Giao Hàng'> <LocalShippingIcon /> </Tooltip>
                        </Button> : ''}
                        {e?.delivery === "SUCCESS" ? <Tooltip title='Giao Hàng Thành Công'><CheckCircleIcon color='success' /></Tooltip> : ''}
                        {e?.delivery === "CANCEL" ? <Tooltip title='Đã Hủy Hàng'><CancelIcon color='error' /> </Tooltip> : ''}
                      </TableCell>
                    </TableRow>
                  )
                })}
              </TableBody>
            </Table>
          </TableContainer>
        </div>
      </div>
      {detail &&
        <BillConfirm
          open={openConfirm}
          handleClose={handleCloseConfirm}
          detail={detail}
          handleReload={handleReload}
        />
      }
      <Toaster />
      {loading && <LoadingComponent />}
    </div >
  )
}

export default Bills