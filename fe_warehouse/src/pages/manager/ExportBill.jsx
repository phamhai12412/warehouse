import { Button, Chip, FormControl, InputLabel, MenuItem, Paper, Select, Table, TableBody, TableCell, TableContainer, TableHead, TableRow, TextField, Tooltip } from '@mui/material'
import React, { useEffect, useState } from 'react'
import { TIME_OUT, debouncing } from '../../utils/Deboucing';
import { useDispatch, useSelector } from 'react-redux';

import CancelIcon from '@mui/icons-material/Cancel';
import CheckCircleIcon from '@mui/icons-material/CheckCircle';
import CreateNewFolderIcon from '@mui/icons-material/CreateNewFolder';
import { DATA_BILL_EXPORT } from '../../redux/selectors/selectors';
import DeleteIcon from '@mui/icons-material/Delete';
import EditIcon from '@mui/icons-material/Edit';
import { GET_ALL_BILL_EXPORT_BY_STORAGE_ID } from '../../redux/api/service/billExportService';
import LocalShippingIcon from '@mui/icons-material/LocalShipping';
import PrecisionManufacturingIcon from '@mui/icons-material/PrecisionManufacturing';
import { REJECT_BILL } from '../../redux/api/service/billService';
import SettingsIcon from '@mui/icons-material/Settings';
import { useNavigate } from 'react-router-dom';

function ExportBill() {

  const navigate = useNavigate();
  const dispatch = useDispatch();
  const billExport = useSelector(DATA_BILL_EXPORT);

  const [user, setUser] = useState(JSON.parse(localStorage.getItem('user')));

  const [reload, setReload] = useState(false);
  const handleReload = () => setReload(!reload);

  const handleEdit = (id) => {
    navigate(`/manager/bills/edit/${id}`);
  }

  const handleDelete = (id) => {
    dispatch(REJECT_BILL(id));
    setTimeout(() => {
      handleReload();
    }, [])
  }

  const [filter, setFilter] = React.useState('ALL');

  const handleChange = (event) => (setFilter(event.target.value));

  const [search, setSearch] = useState('');
  const handleChangeSearch = (e) => setSearch(e.target.value);

  useEffect(() => {
    if (user) {
      dispatch(GET_ALL_BILL_EXPORT_BY_STORAGE_ID({ delivery: filter, search: search, id: user.storageId }));
    } else {
      navigate('/')
    }

  }, [reload, filter, search])

  return (
    <div>
      <div className='flex justify-center uppercase text-2xl font-semibold'>quản lý đơn xuất hàng</div>
      <div className='flex justify-between'>
        <div className="actions flex items-center gap-2"></div>
        <div className="add_manager">
          <Button variant="contained" className='flex gap-2 items-center' onClick={() => navigate("/manager/bills/create")} > <CreateNewFolderIcon /> <span>Tạo phiếu xuất</span> </Button>
        </div>
      </div>
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
          <TextField id="outlined-basic" size='small' onChange={debouncing(handleChangeSearch, TIME_OUT)} fullWidth label="Tìm kiếm theo điểm kết thúc" name='search' variant="outlined" />
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
                {billExport && billExport.map((e) => {
                  return (
                    <TableRow
                      key={e?.bill.id}
                      sx={{ '&:last-child td, &:last-child th': { border: 0 } }}
                      className='hover:bg-slate-100 transition-all hover:cursor-pointer'
                    >
                      <TableCell onClick={() => navigate(`/manager/bills/detail/${e?.bill.id}`)} align='center'>{e?.bill.id}</TableCell>
                      <TableCell onClick={() => navigate(`/manager/bills/detail/${e?.bill.id}`)} align='center'>{e?.bill.created} </TableCell>
                      <TableCell onClick={() => navigate(`/manager/bills/detail/${e?.bill.id}`)} align='center'>{e?.bill.locationStart.toUpperCase()} </TableCell>
                      <TableCell onClick={() => navigate(`/manager/bills/detail/${e?.bill.id}`)} align='center'>{e?.bill.locationEnd.toUpperCase()}</TableCell>
                      <TableCell onClick={() => navigate(`/manager/bills/detail/${e?.bill.id}`)} align='center'>{e?.bill.shipment.shipName}</TableCell>
                      <TableCell onClick={() => navigate(`/manager/bills/detail/${e?.bill.id}`)} align='center'>{(e?.bill.total + e?.bill.priceShip).toLocaleString()} đ</TableCell>
                      <TableCell onClick={() => navigate(`/manager/bills/detail/${e?.bill.id}`)} align='center'>
                        {e?.delivery === "PENDING" ? <Chip label="CHỜ XÁC NHẬN" color="warning" /> : ''}
                        {e?.delivery === "PREPARE" ? <Chip label="CHUẨN BỊ HÀNG" color="secondary" /> : ''}
                        {e?.delivery === "DELIVERY" ? <Chip label="ĐANG GIAO HÀNG" color="primary" /> : ''}
                        {e?.delivery === "SUCCESS" ? <Chip label="THÀNH CÔNG" color="success" /> : ''}
                        {e?.delivery === "CANCEL" ? <Chip label="HỦY HÀNG" color="error" /> : ''}
                      </TableCell>
                      <TableCell align='center'>
                        {e?.delivery === "PENDING" ?
                          <div className='flex gap-2 justify-center'>
                            <Button variant="contained" onClick={() => handleEdit(e?.bill.id)} color='warning'><Tooltip title='edit'><EditIcon /></Tooltip></Button>
                            <Button variant="contained" onClick={() => handleDelete(e?.bill.id)} color='error'><Tooltip title='delete'><DeleteIcon /></Tooltip></Button>
                          </div> : ''}
                        {e?.delivery === "PREPARE" ? <Tooltip variant="contained" color='secondary'>
                          <PrecisionManufacturingIcon />
                        </Tooltip> : ""}
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

export default ExportBill