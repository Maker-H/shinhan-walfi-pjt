package com.shinhan.walfi.config;

import io.reactivex.Flowable;
import java.math.BigInteger;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import org.web3j.abi.EventEncoder;
import org.web3j.abi.TypeReference;
import org.web3j.abi.datatypes.Address;
import org.web3j.abi.datatypes.Event;
import org.web3j.abi.datatypes.Function;
import org.web3j.abi.datatypes.Type;
import org.web3j.abi.datatypes.Utf8String;
import org.web3j.abi.datatypes.generated.Uint256;
import org.web3j.abi.datatypes.generated.Uint8;
import org.web3j.crypto.Credentials;
import org.web3j.protocol.Web3j;
import org.web3j.protocol.core.DefaultBlockParameter;
import org.web3j.protocol.core.RemoteCall;
import org.web3j.protocol.core.RemoteFunctionCall;
import org.web3j.protocol.core.methods.request.EthFilter;
import org.web3j.protocol.core.methods.response.BaseEventResponse;
import org.web3j.protocol.core.methods.response.Log;
import org.web3j.protocol.core.methods.response.TransactionReceipt;
import org.web3j.tx.Contract;
import org.web3j.tx.TransactionManager;
import org.web3j.tx.gas.ContractGasProvider;

/**
 * <p>Auto generated code.
 * <p><strong>Do not modify!</strong>
 * <p>Please use the <a href="https://docs.web3j.io/command_line.html">web3j command line tools</a>,
 * or the org.web3j.codegen.SolidityFunctionWrapperGenerator in the 
 * <a href="https://github.com/web3j/web3j/tree/master/codegen">codegen module</a> to update.
 *
 * <p>Generated with web3j version 1.5.0.
 */
@SuppressWarnings("rawtypes")
public class WALFI extends Contract {
    public static final String BINARY = "0x608060405234801562000010575f80fd5b506040518060400160405280600681526020017f57414c46492000000000000000000000000000000000000000000000000000008152505f9081620000569190620003d9565b506040518060400160405280600a81526020017f57414c464920436f696e00000000000000000000000000000000000000000000815250600190816200009d9190620003d9565b506002805f6101000a81548160ff021916908360ff160217905550620186a060038190555060035460045f3373ffffffffffffffffffffffffffffffffffffffff1673ffffffffffffffffffffffffffffffffffffffff1681526020019081526020015f20819055503373ffffffffffffffffffffffffffffffffffffffff165f73ffffffffffffffffffffffffffffffffffffffff167fddf252ad1be2c89b69c2b068fc378daa952ba7f163c4a11628f55a4df523b3ef600354604051620001679190620004ce565b60405180910390a3620004e9565b5f81519050919050565b7f4e487b71000000000000000000000000000000000000000000000000000000005f52604160045260245ffd5b7f4e487b71000000000000000000000000000000000000000000000000000000005f52602260045260245ffd5b5f6002820490506001821680620001f157607f821691505b602082108103620002075762000206620001ac565b5b50919050565b5f819050815f5260205f209050919050565b5f6020601f8301049050919050565b5f82821b905092915050565b5f600883026200026b7fffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffff826200022e565b6200027786836200022e565b95508019841693508086168417925050509392505050565b5f819050919050565b5f819050919050565b5f620002c1620002bb620002b5846200028f565b62000298565b6200028f565b9050919050565b5f819050919050565b620002dc83620002a1565b620002f4620002eb82620002c8565b8484546200023a565b825550505050565b5f90565b6200030a620002fc565b62000317818484620002d1565b505050565b5b818110156200033e57620003325f8262000300565b6001810190506200031d565b5050565b601f8211156200038d5762000357816200020d565b62000362846200021f565b8101602085101562000372578190505b6200038a62000381856200021f565b8301826200031c565b50505b505050565b5f82821c905092915050565b5f620003af5f198460080262000392565b1980831691505092915050565b5f620003c983836200039e565b9150826002028217905092915050565b620003e48262000175565b67ffffffffffffffff8111156200040057620003ff6200017f565b5b6200040c8254620001d9565b6200041982828562000342565b5f60209050601f8311600181146200044f575f84156200043a578287015190505b620004468582620003bc565b865550620004b5565b601f1984166200045f866200020d565b5f5b82811015620004885784890151825560018201915060208501945060208101905062000461565b86831015620004a85784890151620004a4601f8916826200039e565b8355505b6001600288020188555050505b505050505050565b620004c8816200028f565b82525050565b5f602082019050620004e35f830184620004bd565b92915050565b61102c80620004f75f395ff3fe608060405260043610610094575f3560e01c806370a082311161005857806370a082311461019757806395d89b41146101d3578063a9059cbb146101fd578063cae9ca5114610239578063dd62ed3e146102755761009d565b806306fdde03146100a1578063095ea7b3146100cb57806318160ddd1461010757806323b872dd14610131578063313ce5671461016d5761009d565b3661009d575f80fd5b5f80fd5b3480156100ac575f80fd5b506100b56102b1565b6040516100c29190610a7f565b60405180910390f35b3480156100d6575f80fd5b506100f160048036038101906100ec9190610b3d565b61033d565b6040516100fe9190610b95565b60405180910390f35b348015610112575f80fd5b5061011b61042a565b6040516101289190610bbd565b60405180910390f35b34801561013c575f80fd5b5061015760048036038101906101529190610bd6565b61047b565b6040516101649190610b95565b60405180910390f35b348015610178575f80fd5b50610181610620565b60405161018e9190610c41565b60405180910390f35b3480156101a2575f80fd5b506101bd60048036038101906101b89190610c5a565b610632565b6040516101ca9190610bbd565b60405180910390f35b3480156101de575f80fd5b506101e7610678565b6040516101f49190610a7f565b60405180910390f35b348015610208575f80fd5b50610223600480360381019061021e9190610b3d565b610703565b6040516102309190610b95565b60405180910390f35b348015610244575f80fd5b5061025f600480360381019061025a9190610db1565b610819565b60405161026c9190610b95565b60405180910390f35b348015610280575f80fd5b5061029b60048036038101906102969190610e1d565b610973565b6040516102a89190610bbd565b60405180910390f35b600180546102be90610e88565b80601f01602080910402602001604051908101604052809291908181526020018280546102ea90610e88565b80156103355780601f1061030c57610100808354040283529160200191610335565b820191905f5260205f20905b81548152906001019060200180831161031857829003601f168201915b505050505081565b5f8160055f3373ffffffffffffffffffffffffffffffffffffffff1673ffffffffffffffffffffffffffffffffffffffff1681526020019081526020015f205f8573ffffffffffffffffffffffffffffffffffffffff1673ffffffffffffffffffffffffffffffffffffffff1681526020019081526020015f20819055508273ffffffffffffffffffffffffffffffffffffffff163373ffffffffffffffffffffffffffffffffffffffff167f8c5be1e5ebec7d5bd14f71427d1e84f3dd0314c0f7b2291e5b200ac8c7c3b925846040516104189190610bbd565b60405180910390a36001905092915050565b5f60045f8073ffffffffffffffffffffffffffffffffffffffff1673ffffffffffffffffffffffffffffffffffffffff1681526020019081526020015f20546003546104769190610ee5565b905090565b5f8160045f8673ffffffffffffffffffffffffffffffffffffffff1673ffffffffffffffffffffffffffffffffffffffff1681526020019081526020015f205f8282546104c89190610ee5565b925050819055508160055f8673ffffffffffffffffffffffffffffffffffffffff1673ffffffffffffffffffffffffffffffffffffffff1681526020019081526020015f205f3373ffffffffffffffffffffffffffffffffffffffff1673ffffffffffffffffffffffffffffffffffffffff1681526020019081526020015f205f8282546105569190610ee5565b925050819055508160045f8573ffffffffffffffffffffffffffffffffffffffff1673ffffffffffffffffffffffffffffffffffffffff1681526020019081526020015f205f8282546105a99190610f18565b925050819055508273ffffffffffffffffffffffffffffffffffffffff168473ffffffffffffffffffffffffffffffffffffffff167fddf252ad1be2c89b69c2b068fc378daa952ba7f163c4a11628f55a4df523b3ef8460405161060d9190610bbd565b60405180910390a3600190509392505050565b60025f9054906101000a900460ff1681565b5f60045f8373ffffffffffffffffffffffffffffffffffffffff1673ffffffffffffffffffffffffffffffffffffffff1681526020019081526020015f20549050919050565b5f805461068490610e88565b80601f01602080910402602001604051908101604052809291908181526020018280546106b090610e88565b80156106fb5780601f106106d2576101008083540402835291602001916106fb565b820191905f5260205f20905b8154815290600101906020018083116106de57829003601f168201915b505050505081565b5f8160045f3373ffffffffffffffffffffffffffffffffffffffff1673ffffffffffffffffffffffffffffffffffffffff1681526020019081526020015f205f8282546107509190610ee5565b925050819055508160045f8573ffffffffffffffffffffffffffffffffffffffff1673ffffffffffffffffffffffffffffffffffffffff1681526020019081526020015f205f8282546107a39190610f18565b925050819055508273ffffffffffffffffffffffffffffffffffffffff163373ffffffffffffffffffffffffffffffffffffffff167fddf252ad1be2c89b69c2b068fc378daa952ba7f163c4a11628f55a4df523b3ef846040516108079190610bbd565b60405180910390a36001905092915050565b5f8260055f3373ffffffffffffffffffffffffffffffffffffffff1673ffffffffffffffffffffffffffffffffffffffff1681526020019081526020015f205f8673ffffffffffffffffffffffffffffffffffffffff1673ffffffffffffffffffffffffffffffffffffffff1681526020019081526020015f20819055508373ffffffffffffffffffffffffffffffffffffffff163373ffffffffffffffffffffffffffffffffffffffff167f8c5be1e5ebec7d5bd14f71427d1e84f3dd0314c0f7b2291e5b200ac8c7c3b925856040516108f49190610bbd565b60405180910390a38373ffffffffffffffffffffffffffffffffffffffff16638f4ffcb1338530866040518563ffffffff1660e01b815260040161093b9493929190610fac565b5f604051808303815f87803b158015610952575f80fd5b505af1158015610964573d5f803e3d5ffd5b50505050600190509392505050565b5f60055f8473ffffffffffffffffffffffffffffffffffffffff1673ffffffffffffffffffffffffffffffffffffffff1681526020019081526020015f205f8373ffffffffffffffffffffffffffffffffffffffff1673ffffffffffffffffffffffffffffffffffffffff1681526020019081526020015f2054905092915050565b5f81519050919050565b5f82825260208201905092915050565b5f5b83811015610a2c578082015181840152602081019050610a11565b5f8484015250505050565b5f601f19601f8301169050919050565b5f610a51826109f5565b610a5b81856109ff565b9350610a6b818560208601610a0f565b610a7481610a37565b840191505092915050565b5f6020820190508181035f830152610a978184610a47565b905092915050565b5f604051905090565b5f80fd5b5f80fd5b5f73ffffffffffffffffffffffffffffffffffffffff82169050919050565b5f610ad982610ab0565b9050919050565b610ae981610acf565b8114610af3575f80fd5b50565b5f81359050610b0481610ae0565b92915050565b5f819050919050565b610b1c81610b0a565b8114610b26575f80fd5b50565b5f81359050610b3781610b13565b92915050565b5f8060408385031215610b5357610b52610aa8565b5b5f610b6085828601610af6565b9250506020610b7185828601610b29565b9150509250929050565b5f8115159050919050565b610b8f81610b7b565b82525050565b5f602082019050610ba85f830184610b86565b92915050565b610bb781610b0a565b82525050565b5f602082019050610bd05f830184610bae565b92915050565b5f805f60608486031215610bed57610bec610aa8565b5b5f610bfa86828701610af6565b9350506020610c0b86828701610af6565b9250506040610c1c86828701610b29565b9150509250925092565b5f60ff82169050919050565b610c3b81610c26565b82525050565b5f602082019050610c545f830184610c32565b92915050565b5f60208284031215610c6f57610c6e610aa8565b5b5f610c7c84828501610af6565b91505092915050565b5f80fd5b5f80fd5b7f4e487b71000000000000000000000000000000000000000000000000000000005f52604160045260245ffd5b610cc382610a37565b810181811067ffffffffffffffff82111715610ce257610ce1610c8d565b5b80604052505050565b5f610cf4610a9f565b9050610d008282610cba565b919050565b5f67ffffffffffffffff821115610d1f57610d1e610c8d565b5b610d2882610a37565b9050602081019050919050565b828183375f83830152505050565b5f610d55610d5084610d05565b610ceb565b905082815260208101848484011115610d7157610d70610c89565b5b610d7c848285610d35565b509392505050565b5f82601f830112610d9857610d97610c85565b5b8135610da8848260208601610d43565b91505092915050565b5f805f60608486031215610dc857610dc7610aa8565b5b5f610dd586828701610af6565b9350506020610de686828701610b29565b925050604084013567ffffffffffffffff811115610e0757610e06610aac565b5b610e1386828701610d84565b9150509250925092565b5f8060408385031215610e3357610e32610aa8565b5b5f610e4085828601610af6565b9250506020610e5185828601610af6565b9150509250929050565b7f4e487b71000000000000000000000000000000000000000000000000000000005f52602260045260245ffd5b5f6002820490506001821680610e9f57607f821691505b602082108103610eb257610eb1610e5b565b5b50919050565b7f4e487b71000000000000000000000000000000000000000000000000000000005f52601160045260245ffd5b5f610eef82610b0a565b9150610efa83610b0a565b9250828203905081811115610f1257610f11610eb8565b5b92915050565b5f610f2282610b0a565b9150610f2d83610b0a565b9250828201905080821115610f4557610f44610eb8565b5b92915050565b610f5481610acf565b82525050565b5f81519050919050565b5f82825260208201905092915050565b5f610f7e82610f5a565b610f888185610f64565b9350610f98818560208601610a0f565b610fa181610a37565b840191505092915050565b5f608082019050610fbf5f830187610f4b565b610fcc6020830186610bae565b610fd96040830185610f4b565b8181036060830152610feb8184610f74565b90509594505050505056fea2646970667358221220bc807a2584ef57ac03b0f1b569b1a51045319e1e1a5a132f5e8283019cf0871f64736f6c63430008150033";

    public static final String FUNC_DECIMALS = "decimals";

    public static final String FUNC_NAME = "name";

    public static final String FUNC_SYMBOL = "symbol";

    public static final String FUNC_TOTALSUPPLY = "totalSupply";

    public static final String FUNC_BALANCEOF = "balanceOf";

    public static final String FUNC_TRANSFER = "transfer";

    public static final String FUNC_APPROVE = "approve";

    public static final String FUNC_TRANSFERFROM = "transferFrom";

    public static final String FUNC_ALLOWANCE = "allowance";

    public static final String FUNC_APPROVEANDCALL = "approveAndCall";

    public static final Event APPROVAL_EVENT = new Event("Approval", 
            Arrays.<TypeReference<?>>asList(new TypeReference<Address>(true) {}, new TypeReference<Address>(true) {}, new TypeReference<Uint256>() {}));
    ;

    public static final Event TRANSFER_EVENT = new Event("Transfer", 
            Arrays.<TypeReference<?>>asList(new TypeReference<Address>(true) {}, new TypeReference<Address>(true) {}, new TypeReference<Uint256>() {}));
    ;

    protected static final HashMap<String, String> _addresses;

    static {
        _addresses = new HashMap<String, String>();
    }

    @Deprecated
    public WALFI(String contractAddress, Web3j web3j, Credentials credentials, BigInteger gasPrice, BigInteger gasLimit) {
        super(BINARY, contractAddress, web3j, credentials, gasPrice, gasLimit);
    }

    protected WALFI(String contractAddress, Web3j web3j, Credentials credentials, ContractGasProvider contractGasProvider) {
        super(BINARY, contractAddress, web3j, credentials, contractGasProvider);
    }

    @Deprecated
    protected WALFI(String contractAddress, Web3j web3j, TransactionManager transactionManager, BigInteger gasPrice, BigInteger gasLimit) {
        super(BINARY, contractAddress, web3j, transactionManager, gasPrice, gasLimit);
    }

    protected WALFI(String contractAddress, Web3j web3j, TransactionManager transactionManager, ContractGasProvider contractGasProvider) {
        super(BINARY, contractAddress, web3j, transactionManager, contractGasProvider);
    }

//    public static List<ApprovalEventResponse> getApprovalEvents(TransactionReceipt transactionReceipt) {
//        List<EventValuesWithLog> valueList = staticExtractEventParametersWithLog(APPROVAL_EVENT, transactionReceipt);
//        ArrayList<ApprovalEventResponse> responses = new ArrayList<ApprovalEventResponse>(valueList.size());
//        for (EventValuesWithLog eventValues : valueList) {
//            ApprovalEventResponse typedResponse = new ApprovalEventResponse();
//            typedResponse.log = eventValues.getLog();
//            typedResponse.tokenOwner = (String) eventValues.getIndexedValues().get(0).getValue();
//            typedResponse.spender = (String) eventValues.getIndexedValues().get(1).getValue();
//            typedResponse.tokens = (BigInteger) eventValues.getNonIndexedValues().get(0).getValue();
//            responses.add(typedResponse);
//        }
//        return responses;
//    }

    public static ApprovalEventResponse getApprovalEventFromLog(Log log) {
        EventValuesWithLog eventValues = staticExtractEventParametersWithLog(APPROVAL_EVENT, log);
        ApprovalEventResponse typedResponse = new ApprovalEventResponse();
        typedResponse.log = log;
        typedResponse.tokenOwner = (String) eventValues.getIndexedValues().get(0).getValue();
        typedResponse.spender = (String) eventValues.getIndexedValues().get(1).getValue();
        typedResponse.tokens = (BigInteger) eventValues.getNonIndexedValues().get(0).getValue();
        return typedResponse;
    }

    public Flowable<ApprovalEventResponse> approvalEventFlowable(EthFilter filter) {
        return web3j.ethLogFlowable(filter).map(log -> getApprovalEventFromLog(log));
    }

    public Flowable<ApprovalEventResponse> approvalEventFlowable(DefaultBlockParameter startBlock, DefaultBlockParameter endBlock) {
        EthFilter filter = new EthFilter(startBlock, endBlock, getContractAddress());
        filter.addSingleTopic(EventEncoder.encode(APPROVAL_EVENT));
        return approvalEventFlowable(filter);
    }

//    public static List<TransferEventResponse> getTransferEvents(TransactionReceipt transactionReceipt) {
//        List<EventValuesWithLog> valueList = staticExtractEventParametersWithLog(TRANSFER_EVENT, transactionReceipt);
//        ArrayList<TransferEventResponse> responses = new ArrayList<TransferEventResponse>(valueList.size());
//        for (EventValuesWithLog eventValues : valueList) {
//            TransferEventResponse typedResponse = new TransferEventResponse();
//            typedResponse.log = eventValues.getLog();
//            typedResponse.from = (String) eventValues.getIndexedValues().get(0).getValue();
//            typedResponse.to = (String) eventValues.getIndexedValues().get(1).getValue();
//            typedResponse.tokens = (BigInteger) eventValues.getNonIndexedValues().get(0).getValue();
//            responses.add(typedResponse);
//        }
//        return responses;
//    }

    public static TransferEventResponse getTransferEventFromLog(Log log) {
        EventValuesWithLog eventValues = staticExtractEventParametersWithLog(TRANSFER_EVENT, log);
        TransferEventResponse typedResponse = new TransferEventResponse();
        typedResponse.log = log;
        typedResponse.from = (String) eventValues.getIndexedValues().get(0).getValue();
        typedResponse.to = (String) eventValues.getIndexedValues().get(1).getValue();
        typedResponse.tokens = (BigInteger) eventValues.getNonIndexedValues().get(0).getValue();
        return typedResponse;
    }

    public Flowable<TransferEventResponse> transferEventFlowable(EthFilter filter) {
        return web3j.ethLogFlowable(filter).map(log -> getTransferEventFromLog(log));
    }

    public Flowable<TransferEventResponse> transferEventFlowable(DefaultBlockParameter startBlock, DefaultBlockParameter endBlock) {
        EthFilter filter = new EthFilter(startBlock, endBlock, getContractAddress());
        filter.addSingleTopic(EventEncoder.encode(TRANSFER_EVENT));
        return transferEventFlowable(filter);
    }

    public RemoteFunctionCall<BigInteger> decimals() {
        final Function function = new Function(FUNC_DECIMALS, 
                Arrays.<Type>asList(), 
                Arrays.<TypeReference<?>>asList(new TypeReference<Uint8>() {}));
        return executeRemoteCallSingleValueReturn(function, BigInteger.class);
    }

    public RemoteFunctionCall<String> name() {
        final Function function = new Function(FUNC_NAME, 
                Arrays.<Type>asList(), 
                Arrays.<TypeReference<?>>asList(new TypeReference<Utf8String>() {}));
        return executeRemoteCallSingleValueReturn(function, String.class);
    }

    public RemoteFunctionCall<String> symbol() {
        final Function function = new Function(FUNC_SYMBOL, 
                Arrays.<Type>asList(), 
                Arrays.<TypeReference<?>>asList(new TypeReference<Utf8String>() {}));
        return executeRemoteCallSingleValueReturn(function, String.class);
    }

    public RemoteFunctionCall<BigInteger> totalSupply() {
        final Function function = new Function(FUNC_TOTALSUPPLY, 
                Arrays.<Type>asList(), 
                Arrays.<TypeReference<?>>asList(new TypeReference<Uint256>() {}));
        return executeRemoteCallSingleValueReturn(function, BigInteger.class);
    }

    public RemoteFunctionCall<BigInteger> balanceOf(String tokenOwner) {
        final Function function = new Function(FUNC_BALANCEOF, 
                Arrays.<Type>asList(new Address(tokenOwner)),
                Arrays.<TypeReference<?>>asList(new TypeReference<Uint256>() {}));
        return executeRemoteCallSingleValueReturn(function, BigInteger.class);
    }

    public RemoteFunctionCall<TransactionReceipt> transfer(String to, BigInteger tokens) {
        final Function function = new Function(
                FUNC_TRANSFER, 
                Arrays.<Type>asList(new Address(to),
                new Uint256(tokens)),
                Collections.<TypeReference<?>>emptyList());
        return executeRemoteCallTransaction(function);
    }

    public RemoteFunctionCall<TransactionReceipt> approve(String spender, BigInteger tokens) {
        final Function function = new Function(
                FUNC_APPROVE, 
                Arrays.<Type>asList(new Address(spender),
                new Uint256(tokens)),
                Collections.<TypeReference<?>>emptyList());
        return executeRemoteCallTransaction(function);
    }

    public RemoteFunctionCall<TransactionReceipt> transferFrom(String from, String to, BigInteger tokens) {
        final Function function = new Function(
                FUNC_TRANSFERFROM, 
                Arrays.<Type>asList(new Address(from),
                new Address(to),
                new Uint256(tokens)),
                Collections.<TypeReference<?>>emptyList());
        return executeRemoteCallTransaction(function);
    }

    public RemoteFunctionCall<BigInteger> allowance(String tokenOwner, String spender) {
        final Function function = new Function(FUNC_ALLOWANCE, 
                Arrays.<Type>asList(new Address(tokenOwner),
                new Address(spender)),
                Arrays.<TypeReference<?>>asList(new TypeReference<Uint256>() {}));
        return executeRemoteCallSingleValueReturn(function, BigInteger.class);
    }

    public RemoteFunctionCall<TransactionReceipt> approveAndCall(String spender, BigInteger tokens, byte[] data) {
        final Function function = new Function(
                FUNC_APPROVEANDCALL, 
                Arrays.<Type>asList(new Address(spender),
                new Uint256(tokens),
                new org.web3j.abi.datatypes.DynamicBytes(data)), 
                Collections.<TypeReference<?>>emptyList());
        return executeRemoteCallTransaction(function);
    }

    @Deprecated
    public static WALFI load(String contractAddress, Web3j web3j, Credentials credentials, BigInteger gasPrice, BigInteger gasLimit) {
        return new WALFI(contractAddress, web3j, credentials, gasPrice, gasLimit);
    }

    @Deprecated
    public static WALFI load(String contractAddress, Web3j web3j, TransactionManager transactionManager, BigInteger gasPrice, BigInteger gasLimit) {
        return new WALFI(contractAddress, web3j, transactionManager, gasPrice, gasLimit);
    }

    public static WALFI load(String contractAddress, Web3j web3j, Credentials credentials, ContractGasProvider contractGasProvider) {
        return new WALFI(contractAddress, web3j, credentials, contractGasProvider);
    }

    public static WALFI load(String contractAddress, Web3j web3j, TransactionManager transactionManager, ContractGasProvider contractGasProvider) {
        return new WALFI(contractAddress, web3j, transactionManager, contractGasProvider);
    }

    public static RemoteCall<WALFI> deploy(Web3j web3j, Credentials credentials, ContractGasProvider contractGasProvider) {
        return deployRemoteCall(WALFI.class, web3j, credentials, contractGasProvider, BINARY, "");
    }

    public static RemoteCall<WALFI> deploy(Web3j web3j, TransactionManager transactionManager, ContractGasProvider contractGasProvider) {
        return deployRemoteCall(WALFI.class, web3j, transactionManager, contractGasProvider, BINARY, "");
    }

    @Deprecated
    public static RemoteCall<WALFI> deploy(Web3j web3j, Credentials credentials, BigInteger gasPrice, BigInteger gasLimit) {
        return deployRemoteCall(WALFI.class, web3j, credentials, gasPrice, gasLimit, BINARY, "");
    }

    @Deprecated
    public static RemoteCall<WALFI> deploy(Web3j web3j, TransactionManager transactionManager, BigInteger gasPrice, BigInteger gasLimit) {
        return deployRemoteCall(WALFI.class, web3j, transactionManager, gasPrice, gasLimit, BINARY, "");
    }

    protected String getStaticDeployedAddress(String networkId) {
        return _addresses.get(networkId);
    }

    public static String getPreviouslyDeployedAddress(String networkId) {
        return _addresses.get(networkId);
    }

    public static class ApprovalEventResponse extends BaseEventResponse {
        public String tokenOwner;

        public String spender;

        public BigInteger tokens;
    }

    public static class TransferEventResponse extends BaseEventResponse {
        public String from;

        public String to;

        public BigInteger tokens;
    }
}
