import { Button, Pagination, Paper, Table, TableBody, TableCell, TableContainer, TableHead, TableRow, TextField } from '@mui/material'
import React, { useEffect, useState } from 'react'
import { TIME_OUT, debouncing } from '../../utils/Deboucing';
import { useDispatch, useSelector } from 'react-redux';

import CheckCircleIcon from '@mui/icons-material/CheckCircle';
import { DATA_INVENTORY } from '../../redux/selectors/selectors';
import FileUploadIcon from '@mui/icons-material/FileUpload';
import FormInventory from '../../components/form/FormInventory';
import { GET_ALL_INVENTORY_BY_STORAGE_ID } from '../../redux/api/service/inventoryService';
import InventoryIcon from '@mui/icons-material/Inventory';
import { exportExcel } from '../../utils/ExportExcel';
import { useNavigate } from 'react-router-dom';
import { validateInventoryDate } from '../../utils/ValidateInventory';

function InventoryMarket() {

  const [user, setUser] = useState(JSON.parse(localStorage.getItem('user')));

  const inventory = useSelector(DATA_INVENTORY);
  const dispatch = useDispatch();
  const navigate = useNavigate();

  const [reload, setReload] = useState(false);
  const handleReload = () => setReload(!reload);

  const [toggleAdd, setToggleAdd] = useState(false);
  const handleCreateForm = () => setToggleAdd(true);
  const handleCloseForm = () => setToggleAdd(false);

  const handleExportExcelInventory = async (id) => {
    await exportExcel(`/api/v1/inventory/export/${id}`, `inventory_${id}`);
  }

  const [search, setSearch] = useState('')
  const handleChangeSearch = (e) => setSearch(e.target.value);

  useEffect(() => {
    if (user) {
      dispatch(GET_ALL_INVENTORY_BY_STORAGE_ID({ search: search, id: user.storageId }));
    } else {
      navigate('/')
    }
  }, [search, reload])

  return (
    <div>
      <div className='flex justify-center uppercase text-2xl font-semibold'>quản lý tồn kho</div>
      <div className='flex flex-col gap-5 '>
        <div className='flex justify-between'>
          <div className="actions">
          </div>
          <div className="add_manager">
            {inventory && validateInventoryDate(inventory) ?
              <Button variant="contained" className='flex gap-2' disabled >
                <InventoryIcon /> <span>Tạo Đơn Tồn Kho</span>
              </Button> :
              <Button variant="contained" className='flex gap-2' onClick={handleCreateForm} > <InventoryIcon /> <span>Tạo Đơn Tồn Kho</span> </Button>
            }
          </div>
        </div>
        <div className="header bg-white p-6 shadow-md">
          <TextField onChange={debouncing(handleChangeSearch, TIME_OUT)} id="outlined-basic" size='small' fullWidth label="Tìm kiếm theo báo cáo" name='search' variant="outlined" />
        </div>
      </div>
      <div className="content w-full mt-5">
        <TableContainer component={Paper} >
          <Table sx={{ width: '100%' }} aria-label="simple table">
            <TableHead>
              <TableRow>
                <TableCell align="center">STT</TableCell>
                <TableCell align="center">NGÀY TẠO</TableCell>
                <TableCell align='center'>BÁO CÁO</TableCell>
                <TableCell align='center'>HÀNH ĐỘNG</TableCell>
              </TableRow>
            </TableHead>
            <TableBody>
              {toggleAdd &&
                <FormInventory handleCloseForm={handleCloseForm} handleReload={handleReload} />
              }
              {inventory && inventory.map((item, index) => {
                return (
                  <TableRow
                    key={item?.id}
                    sx={{ '&:last-child td, &:last-child th': { border: 0 } }}
                    className='hover:bg-slate-200 hover:cursor-pointer transition-all'
                  >
                    <TableCell onClick={() => navigate(`/market/reports/inventory/detail/${item.id}/${item.created}`)} align='center'>{index + 1}</TableCell>
                    <TableCell onClick={() => navigate(`/market/reports/inventory/detail/${item.id}/${item.created}`)} align='center'>{item?.created} </TableCell>
                    <TableCell onClick={() => navigate(`/market/reports/inventory/detail/${item.id}/${item.created}`)} align='center'>{item?.note}</TableCell>
                    <TableCell align='center'>
                      <Button onClick={handleExportExcelInventory} variant="outlined" className='flex gap-2'> <FileUploadIcon />
                        <span>Xuất File Excel</span>
                      </Button>
                    </TableCell>
                  </TableRow>
                )
              })}
            </TableBody>
          </Table>
        </TableContainer>
      </div>
    </div>
  )
}

export default InventoryMarket