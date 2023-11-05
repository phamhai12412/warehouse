import { Chip, Table, TableBody, TableCell, TableHead, TableRow, Tooltip } from '@mui/material'
import React, { useEffect, useState } from 'react'
import { useDispatch, useSelector } from 'react-redux';

import CheckCircleIcon from '@mui/icons-material/CheckCircle';
import { DATA_BILL_IMPORT } from '../../redux/selectors/selectors';
import { GET_ALL_BILL_IMPORT_BY_STORAGE_ID } from '../../redux/api/service/billImportService';
import { useNavigate } from 'react-router-dom';

function BillSuccess() {

  const navigate = useNavigate();
  const dispatch = useDispatch();
  const billImport = useSelector(DATA_BILL_IMPORT);

  const [user, setUser] = useState(JSON.parse(localStorage.getItem('user')));

  const handleShowDetail = () => {
    if (user.roles[0] === "ROLE_MANAGER") {
      navigate("/manager/bills/import");
    } else {
      navigate("/market/bills/import");
    }
  }

  useEffect(() => {
    if (user) {
      dispatch(GET_ALL_BILL_IMPORT_BY_STORAGE_ID({ delivery: "ALL", search: "", id: user.storageId }));
    } else {
      navigate("/")
    }
  }, []);

  return (
    <div className='bg-white'>
      <div className='w-full px-5 py-3'>
        <h1 className='uppercase font-semibold text-lg'>Phiếu Nhập Thành Công</h1>
      </div>
      <Table sx={{ width: '100%' }} className='bg-white' aria-label="simple table">
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
          {billImport && billImport.map((e) => {
            if (e?.delivery === "SUCCESS") {
              return (
                <TableRow
                  key={e?.bill.id}
                  sx={{ '&:last-child td, &:last-child th': { border: 0 } }}
                  className='hover:bg-slate-100 transition-all hover:cursor-pointer'
                >
                  <TableCell onClick={handleShowDetail} align='center'>{e?.bill.id}</TableCell>
                  <TableCell onClick={handleShowDetail} align='center'>{e?.bill.created} </TableCell>
                  <TableCell onClick={handleShowDetail} align='center'>{e?.bill.locationStart} </TableCell>
                  <TableCell onClick={handleShowDetail} align='center'>{e?.bill.locationEnd}</TableCell>
                  <TableCell onClick={handleShowDetail} align='center'>{e?.bill.shipment.shipName}</TableCell>
                  <TableCell onClick={handleShowDetail} align='center'>{(e?.bill.total + e?.bill.priceShip).toLocaleString()} đ</TableCell>
                  <TableCell onClick={handleShowDetail} align='center'><Chip label={e?.delivery} color="success" /></TableCell>
                  <TableCell align='center'><Tooltip title='Giao Hàng Thành Công'><CheckCircleIcon color='success' /></Tooltip></TableCell>
                </TableRow>
              )
            }
          })}
        </TableBody>
      </Table>
    </div>
  )
}

export default BillSuccess