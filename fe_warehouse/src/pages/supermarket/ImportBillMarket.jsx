import { Chip, FormControl, InputLabel, MenuItem, Paper, Select, Table, TableBody, TableCell, TableContainer, TableHead, TableRow, TextField, Tooltip } from '@mui/material'
import React, { useEffect, useState } from 'react'
import { TIME_OUT, debouncing } from '../../utils/Deboucing';
import { useDispatch, useSelector } from 'react-redux';

import CancelIcon from '@mui/icons-material/Cancel';
import CheckCircleIcon from '@mui/icons-material/CheckCircle';
import { DATA_BILL_IMPORT } from '../../redux/selectors/selectors';
import EditIcon from '@mui/icons-material/Edit';
import { GET_ALL_BILL_IMPORT_BY_STORAGE_ID } from '../../redux/api/service/billImportService';
import LocalShippingIcon from '@mui/icons-material/LocalShipping';
import PrecisionManufacturingIcon from '@mui/icons-material/PrecisionManufacturing';
import WatchLaterIcon from '@mui/icons-material/WatchLater';
import { useNavigate } from 'react-router-dom';

function ImportBillMarket() {

  const navigate = useNavigate();
  const dispatch = useDispatch();
  const billImport = useSelector(DATA_BILL_IMPORT);

  const [user, setUser] = useState(JSON.parse(localStorage.getItem('user')));


  const [filter, setFilter] = React.useState('ALL');

  const handleChange = (event) => (setFilter(event.target.value));

  const [search, setSearch] = useState('');
  const handleChangeSearch = (e) => setSearch(e.target.value);

  useEffect(() => {
    if (user) {
      dispatch(GET_ALL_BILL_IMPORT_BY_STORAGE_ID({ delivery: filter, search: search, id: user.storageId }));
    } else {
      navigate('/')
    }
  }, [filter, search]);

  return (
    <div>
      <div className='flex justify-center uppercase text-2xl font-semibold'>quản lý đơn nhập hàng</div>
      <div className='flex justify-between'></div>
      <div className="content w-full mt-5">
        <div className="header bg-white p-6 shadow-md  flex gap-3">
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
                  <TableCell align='center'>ĐIỂM KẾT THÚC</TableCell>
                  <TableCell align='center'>ĐƠN VỊ VẬN CHUYỂN</TableCell>
                  <TableCell align='center'>TỔNG TIỀN</TableCell>
                  <TableCell align='center'>TRẠNG THÁI</TableCell>
                  <TableCell align='center'>HÀNH ĐỘNG</TableCell>
                </TableRow>
              </TableHead>
              <TableBody>
                {billImport && billImport.map((e) => {
                  return (
                    <TableRow
                      key={e?.bill.id}
                      sx={{ '&:last-child td, &:last-child th': { border: 0 } }}
                      className='hover:bg-slate-100 transition-all hover:cursor-pointer'
                    >
                      <TableCell onClick={() => navigate(`/market/bills/detail/${e?.bill.id}`)} align='center'>{e?.bill.id}</TableCell>
                      <TableCell onClick={() => navigate(`/market/bills/detail/${e?.bill.id}`)} align='center'>{e?.bill.created} </TableCell>
                      <TableCell onClick={() => navigate(`/market/bills/detail/${e?.bill.id}`)} align='center'>{e?.bill.locationStart.toUpperCase()} </TableCell>
                      <TableCell onClick={() => navigate(`/market/bills/detail/${e?.bill.id}`)} align='center'>{e?.bill.locationEnd.toUpperCase()}</TableCell>
                      <TableCell onClick={() => navigate(`/market/bills/detail/${e?.bill.id}`)} align='center'>{e?.bill.shipment.shipName}</TableCell>
                      <TableCell onClick={() => navigate(`/market/bills/detail/${e?.bill.id}`)} align='center'>{(e?.bill.total + e?.bill.priceShip).toLocaleString()} đ</TableCell>
                      <TableCell onClick={() => navigate(`/market/bills/detail/${e?.bill.id}`)} align='center'>
                        {e?.delivery === "PENDING" ? <Chip label="CHỜ XÁC NHẬN" color="warning" /> : ''}
                        {e?.delivery === "PREPARE" ? <Chip label="CHUẨN BỊ HÀNG" color="secondary" /> : ''}
                        {e?.delivery === "DELIVERY" ? <Chip label="ĐANG GIAO HÀNG" color="primary" /> : ''}
                        {e?.delivery === "SUCCESS" ? <Chip label="THÀNH CÔNG" color="success" /> : ''}
                        {e?.delivery === "CANCEL" ? <Chip label="HỦY HÀNG" color="error" /> : ''}
                      </TableCell>
                      <TableCell align='center'>
                        {e?.delivery === "PENDING" ?
                          <Tooltip title='Đang Chờ Xác Nhận'> <WatchLaterIcon color='warning' />  </Tooltip> : ''}
                        {e?.delivery === "PREPARE" ? <Tooltip title='Chuẩn Bị Hàng'><PrecisionManufacturingIcon color='secondary' /></Tooltip> : ""}
                        {e?.delivery === "DELIVERY" ? <Tooltip title='Đang Giao Hàng'> <LocalShippingIcon color='primary' /> </Tooltip> : ''}
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
    </div>
  )
}

export default ImportBillMarket